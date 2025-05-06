package pl.tks.restrent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.tks.reposrent.config.MongoProperties;

@SpringBootApplication(scanBasePackages = {"pl.tks.restrent", "pl.tks.reposrent", "pl.tks.modelrent", "pl.tks.portsrent", "pl.tks.servicerent"})
@EnableConfigurationProperties(MongoProperties.class)
public class RentRestAdapterApplication {
    public static void main(String[] args) {
        SpringApplication.run(RentRestAdapterApplication.class, args);
    }

}
