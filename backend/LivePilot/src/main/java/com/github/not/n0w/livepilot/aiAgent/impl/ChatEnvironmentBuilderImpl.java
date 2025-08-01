package com.github.not.n0w.livepilot.aiAgent.impl;

import com.github.not.n0w.livepilot.aiAgent.ChatEnvironmentBuilder;
import com.github.not.n0w.livepilot.aiAgent.model.AiChatSession;
import com.github.not.n0w.livepilot.aiAgent.model.Message;
import com.github.not.n0w.livepilot.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class ChatEnvironmentBuilderImpl implements ChatEnvironmentBuilder {
    private final ChatRepository chatRepository;

    @Override
    public AiChatSession getRemembered(String chatId) {
        return null; //todo
    }

    @Override
    public AiChatSession getEnvironment(String chatId) {
        return getUserInfo(chatId);
    }

    @Override
    public AiChatSession getUserInfo(String chatId) {
        AiChatSession envRequest = new AiChatSession();
        return envRequest;
    }
}
