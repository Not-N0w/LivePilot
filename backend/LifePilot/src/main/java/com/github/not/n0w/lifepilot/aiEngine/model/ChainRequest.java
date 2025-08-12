package com.github.not.n0w.lifepilot.aiEngine.model;

import com.github.not.n0w.lifepilot.model.Chat;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChainRequest {
    private ChatSession chatSession;
    private Chat chat;
}
