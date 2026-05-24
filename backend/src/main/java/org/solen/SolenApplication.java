package org.solen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.solen")
public class SolenApplication {

    public static void main(String[] args) {
        SpringApplication.run(SolenApplication.class, args);
    }
}
