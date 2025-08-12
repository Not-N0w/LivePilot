package com.github.not.n0w.lifepilot.aiEngine.task;

import com.github.not.n0w.lifepilot.aiEngine.model.ChatSession;
import com.github.not.n0w.lifepilot.model.AiTaskType;
import com.github.not.n0w.lifepilot.model.Chat;

public interface AiTask {
    public AiTaskType getType();
    public ChatSession execute(ChatSession chatSession, Chat chat );

}
