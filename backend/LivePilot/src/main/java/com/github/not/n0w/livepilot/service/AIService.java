package com.github.not.n0w.livepilot.service;

import com.github.not.n0w.livepilot.model.Chat;
import org.springframework.stereotype.Service;

public interface AIService {
    public String sendMessage(String message, String chatId);
    void pushMessageToUser(String message, String chatId);
}
