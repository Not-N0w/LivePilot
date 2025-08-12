package com.github.not.n0w.lifepilot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@PropertySource("classpath:application.properties")
@Component
@Data
public class TelegramBotConfig {

    @Value("${bot.port}")
    private String port;

    @Value("${bot.host}")
    private String host;
}