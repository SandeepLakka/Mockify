package io.github.sandeeplakka.mockify.service;

import io.github.sandeeplakka.mockify.generator.GeneratorRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class DatasetBuilder {

    private final SchemaService schema;
    private final SecureRandom rnd = new SecureRandom();
    private final Map<String, List<Map<String, Object>>> data = new HashMap<>();

    @PostConstruct
    public void init() {
        buildRows();
        wireFKs();
    }

    @EventListener
    public void onSchemaReloaded(SchemaReloadedEvent event) {
        data.clear();
        buildRows();
        wireFKs();
    }

    /**
     * Return the rows for a model (case-insensitive).
     * Never returns null.
     */
    public List<Map<String, Object>> get(String model) {
        if (model == null) return List.of();
        return data.getOrDefault(model.toLowerCase(), List.of());
    }

    private void buildRows() {
        schema.cfgs().forEach((key, cfg) -> {
            List<Map<String, Object>> rows = IntStream.rangeClosed(1, cfg.count())
                    .mapToObj(id -> {
                        Map<String, Object> m = new LinkedHashMap<>();
                        m.put("id", id);
                        cfg.fake().forEach((field, spec) ->
                                m.put(field, GeneratorRegistry.generate(spec))
                        );
                        return m;
                    }).toList();
            data.put(key, rows);
        });
    }

    //TODO: WIP
    private void wireFKs() {
        schema.cfgs().forEach((key, cfg) -> {
            if (cfg.belongsTo() == null) return;
            String parentKey = cfg.belongsTo().toLowerCase();
            List<Map<String, Object>> parents = data.get(parentKey);
            if (parents == null || parents.isEmpty()) return;
            data.get(key).forEach(child -> {
                int fk = 1 + rnd.nextInt(parents.size());
                child.put(parentKey + "Id", fk);
            });
        });
    }
}
