package com.github.not.n0w.lifepilot.service.impl;

import com.github.not.n0w.lifepilot.service.AIService;
import com.github.not.n0w.lifepilot.service.MessageService;
import com.github.not.n0w.lifepilot.service.WhisperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final AIService aiService;
    private final WhisperService whisperService;

    @Override
    public String handleTextMessage(Long userId, String text) {

        String response;

        if(text != null && !text.isEmpty()) {
            response = aiService.sendMessage(userId, text);
        }
        else {
            log.error("text is empty");
            throw new RuntimeException("text is empty");
        }
        return response;
    }

    @Override
    public String handleAudioMessage(Long userId, MultipartFile audioFile) {

        String response;
        if(audioFile != null) {
            String textFromVoice = whisperService.voiceToText(audioFile);
            response = aiService.sendMessage(userId, textFromVoice);
        }
        else {
            log.error("no audio found");
            throw new RuntimeException("no audio found");
        }

        return response;
    }
}
