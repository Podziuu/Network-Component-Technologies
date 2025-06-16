package pl.tks.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.tks.repos.config.MongoProperties;

@SpringBootApplication(scanBasePackages = {"pl.tks.rest", "pl.tks.configmq", "pl.tks.event", "pl.tks.messaging", "pl.tks.repos", "pl.tks.model", "pl.tks.ports", "pl.tks.ports.infrastructure",  "pl.tks.service", "pl.tks.security", "pl.tks.repos.config"})
@EnableConfigurationProperties(MongoProperties.class)
public class UserRestAdapterApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserRestAdapterApplication.class, args);
    }

}
