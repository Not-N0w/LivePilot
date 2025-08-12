package com.github.not.n0w.livepilot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "server.jwt")
@PropertySource("classpath:application.properties")
@Data
public class JwtProperties {
    private String secret;
}