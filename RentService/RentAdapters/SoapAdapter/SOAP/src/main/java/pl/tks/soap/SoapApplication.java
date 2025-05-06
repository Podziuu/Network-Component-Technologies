package pl.tks.soap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
// "pl.tks.securityrent"
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@ComponentScan(basePackages = {"pl.tks.servicerent", "pl.tks.restrent", "pl.tks.portsrent", "pl.tks.soap", "pl.tks.reposrent", "pl.tks.modelrent", })
public class SoapApplication {
    public static void main(String[] args) {
        SpringApplication.run(SoapApplication.class, args);
    }
}
