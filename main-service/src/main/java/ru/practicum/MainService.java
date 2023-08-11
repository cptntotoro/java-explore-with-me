package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class }) // scanBasePackages = "src/main/webapp/WEB-INF/view"
public class MainService {
    public static void main(String[] args) {
        SpringApplication.run(MainService.class, args);
    }
}