package com.github.not.n0w.lifepilot.service;

import com.github.not.n0w.lifepilot.model.Chat;
import org.springframework.stereotype.Service;

public interface AIService {
    public String sendMessage(String message, String chatId);
    void pushMessageToUser(String message, String chatId);
}
