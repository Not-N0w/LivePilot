package com.github.not.n0w.lifepilot.aiEngine.prompt;

import com.github.not.n0w.lifepilot.aiEngine.model.UserSession;
import com.github.not.n0w.lifepilot.aiEngine.model.Message;
import com.github.not.n0w.lifepilot.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BasePromptBuilder {
    private final EnvironmentManager environmentManager;
    private final PromptLoader promptLoader;

    public UserSession getSystemMessages(User user) {
        List<Message> messages = new ArrayList<>();
        messages.add(
                new Message("system", promptLoader.loadPromptText("BasePrompt.txt"))
        );
        log.info("BasePrompt loaded");

        messages.addAll(environmentManager.getEnvironmentalMessages(user));
        log.info("Environmental messages loaded");

        return UserSession.builder()
                        .systemMessages(messages)
                        .build();
    }
}
