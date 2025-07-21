package com.github.not.n0w.livepilot.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostgresStartConfigurator {

    private final JdbcTemplate jdbcTemplate;

    @Value("${ai.history.length}")
    private String historyLength;

    @PostConstruct
    public void setCustomPostgresSetting() {
        jdbcTemplate.execute("SET ai.history.length = '" + historyLength + "'");
    }
}
