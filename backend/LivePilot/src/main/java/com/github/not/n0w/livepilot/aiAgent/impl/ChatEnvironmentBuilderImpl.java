package com.github.not.n0w.livepilot.aiAgent.impl;

import com.github.not.n0w.livepilot.aiAgent.ChatEnvironmentBuilder;
import com.github.not.n0w.livepilot.aiAgent.model.ChatCompletionRequest;
import com.github.not.n0w.livepilot.aiAgent.model.Message;
import com.github.not.n0w.livepilot.model.User;
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
    public ChatCompletionRequest getRemembered(String chatId) {
        return null; //todo
    }

    @Override
    public ChatCompletionRequest getEnvironment(String chatId) {
        return getUserInfo(chatId);
    }

    @Override
    public ChatCompletionRequest getUserInfo(String chatId) {
        ChatCompletionRequest envRequest = new ChatCompletionRequest();

        User user = chatRepository.findUserByChatId(chatId);
        if (user == null) {
            log.error("User not found");
            return null;
        }
        envRequest.addMessage(
                new Message(
                        "system",
                        user.toString()
                )
        );
        return envRequest;
    }
}
