package com.github.not.n0w.lifepilot.service;


import org.springframework.web.multipart.MultipartFile;

public interface MessageService {
    public String handleTextMessage(Long userId, String text);
    public String handleAudioMessage(Long userId, MultipartFile audioFile);

}
