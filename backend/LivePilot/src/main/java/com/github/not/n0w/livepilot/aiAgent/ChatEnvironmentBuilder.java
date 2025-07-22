package com.github.not.n0w.livepilot.aiAgent;

import com.github.not.n0w.livepilot.aiAgent.model.AiChatSession;

public interface ChatEnvironmentBuilder {
    public AiChatSession getUserInfo(String chatId);
    public AiChatSession getRemembered(String chatId);
    public AiChatSession getEnvironment(String chatId);
}
