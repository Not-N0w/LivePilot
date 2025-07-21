package com.github.not.n0w.livepilot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@PropertySource("classpath:application.properties")
public class AIConfig {
    @Value("${ai.history.length}")
    private int historyLength;

    @Value("${ai.openrouter.api-key}")
    private String openrouterApiKey;

    @Value("${ai.openrouter.url}")
    private String openrouterUrl;


    @Bean
    public WebClient aiClient() {
        return WebClient.builder()
                .baseUrl(openrouterUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + openrouterApiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
