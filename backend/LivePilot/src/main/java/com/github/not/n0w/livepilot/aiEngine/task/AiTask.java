package com.github.not.n0w.livepilot.aiEngine.task;

import com.github.not.n0w.livepilot.aiEngine.model.ChatSession;
import com.github.not.n0w.livepilot.model.AiTaskType;
import com.github.not.n0w.livepilot.model.Chat;

public interface AiTask {
    public AiTaskType getType();
    public ChatSession execute(ChatSession chatSession, Chat chat );

}
