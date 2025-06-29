package io.github.sandeeplakka.mockify;

import io.github.sandeeplakka.mockify.generator.GeneratorRegistry;
import io.github.sandeeplakka.mockify.meta.GeneratorMetaProvider;
import io.github.sandeeplakka.mockify.service.DatasetBuilder;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MockifyApplicationTests implements WithAssertions {

    @Autowired
    GeneratorMetaProvider meta;

    @Autowired
    DatasetBuilder builder;

    /* 1  catalog vs registry --------------------------------------- */
    @Test
    void everyCatalogGeneratorIsImplemented() {
        var missing = meta.all().stream()
                .map(m -> m.name().split("\\[")[0].toLowerCase())
                .filter(n -> !GeneratorRegistry.contains(n))
                .distinct()
                .toList();
        assertThat(missing)
                .as("catalog generators that lack implementation")
                .isEmpty();
    }

    /* 2  dataset builds after context startup ---------------------- */
    @Test
    @Disabled("will make sure to add such test in future")
    void datasetBuilderGeneratesRows() {
        var users = builder.get("user");
        assertThat(users).isNotNull();
        // with default sample YAML in resources the dataset is non-empty
        assertThat(users.size()).isGreaterThan(0);
    }
}
