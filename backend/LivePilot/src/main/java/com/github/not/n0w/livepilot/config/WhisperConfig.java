package com.github.not.n0w.livepilot.config;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
@PropertySource("classpath:application.properties")
public class WhisperConfig {

    @Value("${ai.whisper.host}")
    private String whisperHost;

    @Value("${ai.whisper.port}")
    private int whisperPort;

    @Bean
    public WebClient whisperClient() {
        HttpClient client = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(60))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);

        return WebClient.builder()
                .baseUrl("http://" + whisperHost + ":" + whisperPort)
                .clientConnector(new ReactorClientHttpConnector(client))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}