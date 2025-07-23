package com.github.not.n0w.livepilot.aiEngine.model;

import com.github.not.n0w.livepilot.model.Chat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AiRequest {
    private Chat chat;
    private List<Message> messages;
}
