package it.p.lodz.pl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "repo")
public class SoapApplication {
    public static void main(String[] args) {
        SpringApplication.run(SoapApplication.class, args);
    }
}
