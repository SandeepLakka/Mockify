package io.github.sandeeplakka.mockify;

import io.github.sandeeplakka.mockify.config.SchemaStorageProperties;
import io.github.sandeeplakka.mockify.service.SchemaService;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class SchemaServiceTests {

    @Test
    void loadEmptyFileReturnsEmptyConfig() throws IOException {
        Path temp = Files.createTempFile("schema", ".yml");
        SchemaStorageProperties props = new SchemaStorageProperties();
        props.setSchemaPath(temp.toString());

        SchemaService service = new SchemaService(props);

        assertThat(service.cfgs()).isEmpty();
    }
}
