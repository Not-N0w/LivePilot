package com.github.not.n0w.livepilot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LivePilotApplication {

    public static void main(String[] args) {
        SpringApplication.run(LivePilotApplication.class, args);
    }

}
