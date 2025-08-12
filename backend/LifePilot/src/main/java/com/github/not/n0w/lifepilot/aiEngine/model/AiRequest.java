package com.github.not.n0w.lifepilot.aiEngine.model;

import com.github.not.n0w.lifepilot.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AiRequest {
    private User user;
    private List<Message> messages;
}
