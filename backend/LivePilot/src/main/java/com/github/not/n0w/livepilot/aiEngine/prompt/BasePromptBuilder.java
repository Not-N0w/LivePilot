package com.github.not.n0w.livepilot.aiEngine.prompt;

import com.github.not.n0w.livepilot.aiEngine.model.ChatSession;
import com.github.not.n0w.livepilot.aiEngine.model.Message;
import com.github.not.n0w.livepilot.model.Chat;
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

    public ChatSession getSystemMessages(Chat chat) {
        List<Message> messages = new ArrayList<>();
        messages.add(
                new Message("system", promptLoader.loadPromptText("BasePrompt.txt"))
        );
        log.info("BasePrompt loaded");

        messages.addAll(environmentManager.getEnvironmentalMessages(chat));
        log.info("Environmental messages loaded");

        return ChatSession.builder()
                        .systemMessages(messages)
                        .build();
    }
}
