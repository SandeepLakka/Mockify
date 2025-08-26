package io.github.sandeeplakka.mockify.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DatasetBuilderRefreshTest {

    private static final Path schemaFile;

    static {
        try {
            Path dir = Files.createTempDirectory("schema-test");
            schemaFile = dir.resolve("schema.yml");
            System.setProperty("mockify.schemaPath", schemaFile.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    private SchemaService schemaService;

    @Autowired
    private DatasetBuilder datasetBuilder;

    @Test
    void datasetRebuildsAfterSchemaSave() {
        assertThat(datasetBuilder.get("user")).isEmpty();

        Map<String, Object> root = Map.of(
                "models", Map.of(
                        "User", Map.of(
                                "_count", 2,
                                "fake", Map.of("name", "name")
                        )
                )
        );

        schemaService.saveYaml(root);
        assertThat(datasetBuilder.get("user")).hasSize(2);

        Map<String, Object> updated = Map.of(
                "models", Map.of(
                        "User", Map.of(
                                "_count", 3,
                                "fake", Map.of("name", "name")
                        )
                )
        );

        schemaService.saveYaml(updated);
        assertThat(datasetBuilder.get("user")).hasSize(3);
    }
}
