package pl.tks.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.tks.repos.config.MongoProperties;

@SpringBootApplication(scanBasePackages = {"pl.tks.rest", "pl.tks.repos", "pl.tks.model", "pl.tks.ports", "pl.tks.service"})
@EnableConfigurationProperties(MongoProperties.class)
public class RentRestAdapterApplication {
    public static void main(String[] args) {
        SpringApplication.run(RentRestAdapterApplication.class, args);
    }

}
