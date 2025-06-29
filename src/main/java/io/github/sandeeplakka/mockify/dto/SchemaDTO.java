package io.github.sandeeplakka.mockify.dto;

import java.util.ArrayList;
import java.util.List;

public record SchemaDTO(List<ModelDTO> models) {
    public SchemaDTO {
        if (models == null) models = new ArrayList<>();
    }
}
