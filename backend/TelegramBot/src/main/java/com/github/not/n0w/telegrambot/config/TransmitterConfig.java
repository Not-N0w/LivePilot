package com.github.not.n0w.telegrambot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Slf4j
public class TransmitterConfig {
    @Value("${main.server.host}")
    private String serverHost;

    @Value("${main.server.port}")
    private int serverPort;

    @Bean
    public WebClient transmitterClient() {
        return WebClient.builder()
                .baseUrl("http://" + serverHost + ":" + serverPort)
                .build();
    }
}
