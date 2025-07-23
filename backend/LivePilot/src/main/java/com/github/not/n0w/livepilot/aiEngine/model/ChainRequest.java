package com.github.not.n0w.livepilot.aiEngine.model;

import com.github.not.n0w.livepilot.model.AiTaskType;
import com.github.not.n0w.livepilot.model.Chat;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChainRequest {
    private ChatSession chatSession;
    private AiTaskType taskType;
    private Chat chat;
}
