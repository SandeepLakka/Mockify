package io.github.sandeeplakka.mockify.schema;

import java.util.List;
import java.util.Map;

public record ModelCfg(
        String name,
        int count,
        String belongsTo,           // FK source (child to parent)
        List<String> hasOne,        // parent to single child
        List<String> hasMany,       // parent to list of children
        Map<String, String> fake
) {
}
