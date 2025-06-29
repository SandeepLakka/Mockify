package io.github.sandeeplakka.mockify.service;

import io.github.sandeeplakka.mockify.dto.FieldDTO;
import io.github.sandeeplakka.mockify.dto.ModelDTO;
import io.github.sandeeplakka.mockify.dto.SchemaDTO;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class YamlMapper {

    /* Map to DTO -------------------------------------------------------- */
    @SuppressWarnings("unchecked")
    SchemaDTO mapToDto(Map<String, Object> root) {
        List<ModelDTO> models = new ArrayList<>();
        Map<String, Object> modelsMap = (Map<String, Object>) root.get("models");
        if (modelsMap != null) {
            modelsMap.forEach((name, cfgObj) -> {
                Map<String, Object> cfg = (Map<String, Object>) cfgObj;
                Integer count = (Integer) cfg.getOrDefault("count", cfg.get("_count"));
                Map<String, String> fake = (Map<String, String>) cfg.get("fake");
                List<FieldDTO> fields = new ArrayList<>();
                if (fake != null) {
                    fake.forEach((k, v) -> fields.add(new FieldDTO(k, v)));
                }
                List<String> hasMany = (List<String>) cfg.getOrDefault("hasMany", List.of());
                List<String> belongsTo = (List<String>) cfg.getOrDefault("belongsTo", List.of());
                models.add(new ModelDTO(name, count, fields, hasMany, belongsTo));
            });
        }
        return new SchemaDTO(models);
    }

    /* DTO to Map -------------------------------------------------------- */
    Map<String, Object> dtoToMap(SchemaDTO dto) {
        Map<String, Object> root = new LinkedHashMap<>();
        Map<String, Object> models = new LinkedHashMap<>();
        dto.models().forEach(m -> {
            Map<String, Object> cfg = new LinkedHashMap<>();
            cfg.put("_count", m.count());
            if (!m.fields().isEmpty()) {
                Map<String, String> fake = new LinkedHashMap<>();
                m.fields().forEach(f -> fake.put(f.name(), f.generator()));
                cfg.put("fake", fake);
            }
            if (!m.hasMany().isEmpty()) cfg.put("hasMany", m.hasMany());
            if (!m.belongsTo().isEmpty()) cfg.put("belongsTo", m.belongsTo());
            models.put(m.name(), cfg);
        });
        root.put("models", models);
        return root;
    }
}
