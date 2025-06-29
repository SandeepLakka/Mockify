package io.github.sandeeplakka.mockify.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "mockify")
public class SchemaStorageProperties {

    /**
     * Where the YAML designed in the UI is stored.
     * Defaults to classpath location for dev; supply an absolute path in prod.
     */
    private String schemaPath = "src/main/resources/jsonSchema.yml";

}
