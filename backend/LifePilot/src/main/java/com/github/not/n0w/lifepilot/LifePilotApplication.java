package com.github.not.n0w.lifepilot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LifePilotApplication {

    public static void main(String[] args) {
        SpringApplication.run(LifePilotApplication.class, args);
    }

}
