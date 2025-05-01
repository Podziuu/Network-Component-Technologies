package pl.tks.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"pl.tks.rest", "pl.tks.repos", "pl.tks.model", "pl.tks.ports", "pl.tks.service", "pl.tks.security"})
public class RentRestAdapterApplication {
    public static void main(String[] args) {
        SpringApplication.run(RentRestAdapterApplication.class, args);
    }

}
