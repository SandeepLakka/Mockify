package io.github.sandeeplakka.mockify.startup;

import io.github.sandeeplakka.mockify.schema.ModelCfg;
import io.github.sandeeplakka.mockify.service.SchemaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class GeneratorContractValidator {

    private final SchemaService schema;

    @Bean
    ApplicationRunner validate() {
        return args -> checkRelations();
    }

    /* ensure every child referenced in hasOne/hasMany
       points back with belongsTo */
    private void checkRelations() {
        var cfgs = schema.cfgs();
        cfgs.values().forEach(parent -> {
            parent.hasOne().forEach(child -> guard(parent, child, "hasOne"));
            parent.hasMany().forEach(child -> guard(parent, child, "hasMany"));
        });
        log.info("✅ Relation topology validated.");
    }

    private void guard(ModelCfg parent, String child, String type) {
        var childCfg = schema.cfgs().get(child.toLowerCase());
        if (childCfg == null || childCfg.belongsTo() == null ||
                !childCfg.belongsTo().equalsIgnoreCase(parent.name()))
            throw new IllegalStateException(
                    "%s %s references %s but it does not belongTo %s"
                            .formatted(type, parent.name(), child, parent.name()));
    }
}
