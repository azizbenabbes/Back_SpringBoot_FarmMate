package com.example.farmmate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling

public class FarmMateApplication {

    public static void main(String[] args) {
        SpringApplication.run(FarmMateApplication.class, args);
    }

}
