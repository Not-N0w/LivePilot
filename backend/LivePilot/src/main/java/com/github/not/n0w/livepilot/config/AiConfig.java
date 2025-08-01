package com.github.not.n0w.livepilot.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

@Configuration
@PropertySource("classpath:application.properties")
@Data
@Slf4j
public class AiConfig {
    @Value("${ai.history.length}")
    private int historyLength;

    @Value("${ai.openrouter.api-key}")
    private String openrouterApiKey;

    @Value("${ai.openrouter.url}")
    private String openrouterUrl;

    @Value("${ai.model}")
    private String aiModel;

    @Bean
    public WebClient aiClient() {
        TcpClient tcpClient = TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 15000)
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(15))
                        .addHandlerLast(new WriteTimeoutHandler(15)));

        HttpClient httpClient = HttpClient.from(tcpClient);

        return WebClient.builder()
                .baseUrl(openrouterUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + openrouterApiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .filter(logRequest())
                .filter(logResponse())
                .build();
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            return Mono.just(clientRequest);
        });
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("Response status: {}", clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }
}
