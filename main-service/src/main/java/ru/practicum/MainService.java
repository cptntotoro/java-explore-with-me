package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication//(exclude = { SecurityAutoConfiguration.class }) // scanBasePackages = "src/main/webapp/WEB-INF/view"
public class MainService {//extends SpringBootServletInitializer  {

//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(MainService.class);
//    }

    public static void main(String[] args) {
        SpringApplication.run(MainService.class, args);
    }
}