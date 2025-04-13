package pl.tks.soap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@ComponentScan(basePackages = {"pl.tks.service", "pl.tks.rest", "pl.tks.ports", "pl.tks.soap", "pl.tks.repos", "pl.tks.model"})
public class SoapApplication {
    public static void main(String[] args) {
        SpringApplication.run(SoapApplication.class, args);
    }
}
