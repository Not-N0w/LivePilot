package com.github.not.n0w.livepilot.aiAgent;

import com.github.not.n0w.livepilot.aiAgent.model.ChatCompletionRequest;

public interface ChatEnvironmentBuilder {
    public ChatCompletionRequest getUserInfo(String chatId);
    public ChatCompletionRequest getRemembered(String chatId);
    public ChatCompletionRequest getEnvironment(String chatId);
}
