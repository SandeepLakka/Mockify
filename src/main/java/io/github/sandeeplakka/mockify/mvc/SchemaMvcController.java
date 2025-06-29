package io.github.sandeeplakka.mockify.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.sandeeplakka.mockify.dto.*;
import io.github.sandeeplakka.mockify.meta.GeneratorMetaProvider;
import io.github.sandeeplakka.mockify.service.SchemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//TODO: WIP
@Controller
@RequestMapping("/schema-builder")
@RequiredArgsConstructor
public class SchemaMvcController {

    private final GeneratorMetaProvider meta;
    private final SchemaService schema;

    @GetMapping
    public String form(Model model) throws JsonProcessingException {
        // Build DTO from your stored ModelCfgs
        List<ModelDTO> dtos = schema.cfgs().values().stream()
                .map(cfg -> new ModelDTO(
                        cfg.name(),
                        cfg.count(),
                        cfg.fake().entrySet().stream()
                                .map(e -> new FieldDTO(e.getKey(), e.getValue()))
                                .collect(Collectors.toList()),
                        cfg.hasMany(),
                        cfg.belongsTo() == null ? List.of() : List.of(cfg.belongsTo())
                ))
                .toList();

        SchemaDTO dto = new SchemaDTO(dtos);
        SchemaForm formBean = Mapper.dtoToForm(dto);

        // Pass real Java objects (not JSON strings) to Thymeleaf
        model.addAttribute("catalog", meta.all());
        model.addAttribute("models", formBean.getModels());
        return "builder";
    }

    @PostMapping
    public String save(@ModelAttribute("schema") SchemaForm form,
                       RedirectAttributes ra) {
        SchemaDTO dto = Mapper.formToDto(form);

        // Turn DTO back into a Map<String,Object> that your SchemaService expects
        Map<String, Object> root = new LinkedHashMap<>();
        Map<String, Object> models = new LinkedHashMap<>();
        for (ModelDTO m : dto.models()) {
            Map<String, Object> cfg = new LinkedHashMap<>();
            cfg.put("_count", m.count());
            if (!m.belongsTo().isEmpty())
                cfg.put("belongsTo", m.belongsTo().get(0));
            if (!m.hasMany().isEmpty())
                cfg.put("hasMany", m.hasMany());
            if (!m.fields().isEmpty()) {
                Map<String, String> fake = new LinkedHashMap<>();
                m.fields().forEach(f -> fake.put(f.name(), f.generator()));
                cfg.put("fake", fake);
            }
            models.put(m.name(), cfg);
        }
        root.put("models", models);

        schema.saveYaml(root);
        ra.addFlashAttribute("msg", "✓ Saved schema, reloaded data");
        return "redirect:/schema-builder";
    }
}
