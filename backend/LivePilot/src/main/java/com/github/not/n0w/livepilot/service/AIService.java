package com.github.not.n0w.livepilot.service;

import com.github.not.n0w.livepilot.model.Chat;
import org.springframework.stereotype.Service;

public interface AIService {
    String sendMessage(String message, String chatId);
}
