package io.github.sandeeplakka.mockify;

import io.github.sandeeplakka.mockify.config.JoinPolicyProperties;
import io.github.sandeeplakka.mockify.config.SchemaStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({SchemaStorageProperties.class, JoinPolicyProperties.class})
public class MockifyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MockifyApplication.class, args);
    }
}