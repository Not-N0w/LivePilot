package com.github.not.n0w.lifepilot.service;

public interface AIService {
    public String sendMessage(Long userId, String message);
    void pushMessageToUser(Long userId, String message);
}
