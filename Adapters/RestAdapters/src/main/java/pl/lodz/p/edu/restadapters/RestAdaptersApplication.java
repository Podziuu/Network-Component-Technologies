package pl.lodz.p.edu.restadapters;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {// Twój obecny pakiet
        "services",      // Ścieżka do pakietu ApplicationCore
        "ui",  // Ścieżka do pakietu ApplicationServices
        "infrastructure",
        "aggregates",
        "repo",
        "security",
        "pl.lodz.p.edu.restadapters.config",
        "pl.lodz.p.edu.restadapters.controller",
        "pl.lodz.p.edu.restadapters.dto",
        "pl.lodz.p.edu.restadapters.exception",
        "pl.lodz.p.edu.restadapters.mapper",
        "pl.lodz.p.edu.restadapters.restsecurity",

})
public class RestAdaptersApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestAdaptersApplication.class, args);
    }

}
