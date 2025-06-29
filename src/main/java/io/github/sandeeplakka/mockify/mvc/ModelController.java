package io.github.sandeeplakka.mockify.mvc;

import io.github.sandeeplakka.mockify.config.JoinPolicyProperties;
import io.github.sandeeplakka.mockify.schema.ModelCfg;
import io.github.sandeeplakka.mockify.service.DatasetBuilder;
import io.github.sandeeplakka.mockify.service.SchemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class ModelController {

    private final DatasetBuilder builder;
    private final SchemaService schema;
    private final JoinPolicyProperties props;

    @GetMapping("{model}")
    public List<?> list(@PathVariable(name = "model") String model,
                        @RequestParam(name = "join", required = false) String join) {

        // resolve policy, then fetch rows (never null):
        var mode = resolve(join);
        return builder.get(model).stream()
                .map(row -> embed(row, model.toLowerCase(), mode, new HashSet<>()))
                .toList();
    }

    private JoinPolicyProperties.Mode resolve(String override) {
        if (override == null || override.isBlank())
            return props.getJoinPolicy();
        return JoinPolicyProperties.Mode.valueOf(override.trim().toUpperCase());
    }

    private Map<String, Object> embed(Map<String, Object> row,
                                      String model,
                                      JoinPolicyProperties.Mode mode,
                                      Set<String> seen) {
        if (mode == JoinPolicyProperties.Mode.FKS) return row;
        if (!seen.add(model)) return row;

        Map<String, Object> out = new LinkedHashMap<>(row);
        Map<String, ModelCfg> cfgs = schema.cfgs();
        ModelCfg cfg = cfgs.get(model);
        if (cfg == null) return out;

        // belongsTo parent
        if (cfg.belongsTo() != null) {
            String p = cfg.belongsTo().toLowerCase();
            Object fk = row.get(p + "Id");
            if (fk instanceof Integer index) {
                List<Map<String, Object>> parents = builder.get(p);
                if (index - 1 < parents.size()) {
                    var parentObj = parents.get(index - 1);
                    out.put(p, embed(parentObj,
                            p,
                            mode == JoinPolicyProperties.Mode.DEEP
                                    ? mode
                                    : JoinPolicyProperties.Mode.FKS,
                            seen));
                }
            }
        }

        // hasOne
        cfg.hasOne().forEach(childName -> {
            String c = childName.toLowerCase();
            builder.get(c).stream()
                    .filter(r -> Objects.equals(r.get(model + "Id"), row.get("id")))
                    .findFirst()
                    .ifPresent(ch -> out.put(c,
                            embed(ch,
                                    c,
                                    mode == JoinPolicyProperties.Mode.DEEP
                                            ? mode
                                            : JoinPolicyProperties.Mode.FKS,
                                    seen)));
        });

        // hasMany (only in DEEP mode)
        if (mode == JoinPolicyProperties.Mode.DEEP) {
            cfg.hasMany().forEach(childName -> {
                String c = childName.toLowerCase();
                List<?> list = builder.get(c).stream()
                        .filter(r -> Objects.equals(r.get(model + "Id"), row.get("id")))
                        .map(ch -> embed(ch, c, mode, seen))
                        .toList();
                out.put(c + "s", list);
            });
        }

        return out;
    }
}
