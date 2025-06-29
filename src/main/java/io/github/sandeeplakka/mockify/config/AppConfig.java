package io.github.sandeeplakka.mockify.config;

import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

@Component
public class AppConfig {

    private final Map<String, Object> root;

    //TODO: need to configure for hot reload based on yml changes
    @SuppressWarnings("unchecked")
    public AppConfig() {
        Yaml yaml = new Yaml();
        InputStream is = getClass().getClassLoader().getResourceAsStream("jsonSchema.yml");
        if (is == null) {
            throw new IllegalStateException("jsonSchema.yml not found on classpath");
        }
        root = yaml.load(is);
    }

    public Map<String, Object> getRoot() {
        return root;
    }
}