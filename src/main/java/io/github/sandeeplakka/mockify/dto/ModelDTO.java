package io.github.sandeeplakka.mockify.dto;

import java.util.ArrayList;
import java.util.List;

public record ModelDTO(String name,
                       Integer count,
                       List<FieldDTO> fields,
                       List<String> hasMany,
                       List<String> belongsTo) {

    public ModelDTO {
        if (fields == null) fields = new ArrayList<>();
        if (hasMany == null) hasMany = new ArrayList<>();
        if (belongsTo == null) belongsTo = new ArrayList<>();
        if (count == null || count < 1) count = 10;
    }
}
