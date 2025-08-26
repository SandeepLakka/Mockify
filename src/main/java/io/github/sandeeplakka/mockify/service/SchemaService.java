package io.github.sandeeplakka.mockify.service;

import io.github.sandeeplakka.mockify.config.SchemaStorageProperties;
import io.github.sandeeplakka.mockify.schema.ModelCfg;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SchemaService {

    private final static Yaml yaml;

    static {
        DumperOptions opts = new DumperOptions();
        opts.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        opts.setPrettyFlow(true); // nicer formatting
        yaml = new Yaml(opts);
    }

    private final SchemaStorageProperties props;
    private Map<String, ModelCfg> cfgs;

    public SchemaService(SchemaStorageProperties props) {
        this.props = props;
        cfgs = load();
    }

    /* helper */
    private static List<String> toList(Object o) {
        if (o == null) return List.of();
        if (o instanceof List l) return l.stream().map(String::valueOf).toList();
        return List.of(String.valueOf(o));
    }

    public Map<String, ModelCfg> cfgs() {
        return cfgs;
    }

    /* ------------ I/O -------------------------------------------- */
    private Map<String, ModelCfg> load() {
        Map<String, Object> root = read();
        if (root == null) {
            root = Map.of("models", Map.of());
        }
        Map<String, Object> raw = (Map<String, Object>) root.getOrDefault("models", Map.of());

        return raw.entrySet().stream().collect(Collectors.toMap(
                e -> e.getKey().toLowerCase(),
                e -> {
                    String n = e.getKey();
                    Map<String, Object> m = (Map<String, Object>) e.getValue();
                    int cnt = (Integer) m.getOrDefault("_count", 10);

                    String belongs = (String) m.get("belongsTo");
                    List<String> one = toList(m.get("hasOne"));
                    List<String> many = toList(m.get("hasMany"));

                    Map<String, String> fake =
                            (Map<String, String>) m.getOrDefault("fake", Map.of());

                    return new ModelCfg(n, cnt, belongs, one, many, fake);
                }));
    }

    private Map<String, Object> read() {
        Path p = Path.of(props.getSchemaPath());
        if (Files.notExists(p)) return Map.of("models", Map.of());
        try (var in = Files.newInputStream(p)) {
            return yaml.load(in);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void saveYaml(Map<String, Object> newRoot) {
        Path p = Path.of(props.getSchemaPath());
        try {
            Files.createDirectories(p.getParent());
            Files.writeString(p, yaml.dump(newRoot));
            cfgs = load();                       // hot-reload
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
