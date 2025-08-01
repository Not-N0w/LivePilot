package com.github.not.n0w.livepilot.aiEngine.prompt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PromptLoader {

    public String loadPromptText(String resourcePath) {
        try {
            ClassPathResource resource = new ClassPathResource("/static/prompts/"+ resourcePath);
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            log.error("Failed to load prompt from " + resourcePath, e);
            return "";
        }
    }
}