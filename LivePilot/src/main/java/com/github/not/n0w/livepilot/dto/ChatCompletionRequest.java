package com.github.not.n0w.livepilot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatCompletionRequest {
    private String model;
    private List<Message> messages;

    public void addSystemMessage(String systemMessage) {
        messages.add(0, new Message("system", systemMessage));
    }
}
