package com.example.baoyan_assistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BaoyanAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaoyanAssistantApplication.class, args);
    }

}
