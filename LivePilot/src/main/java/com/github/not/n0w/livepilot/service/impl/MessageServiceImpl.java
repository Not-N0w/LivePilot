package com.github.not.n0w.livepilot.service.impl;

import com.github.not.n0w.livepilot.dto.RequestDto;
import com.github.not.n0w.livepilot.service.AIService;
import com.github.not.n0w.livepilot.service.MessageService;
import com.github.not.n0w.livepilot.service.WhisperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final AIService aiService;
    private final WhisperService whisperService;

    @Override
    public String handleMessage(RequestDto requestDto) {
        // maybe I should add an extra layer here

        String response;

        if(requestDto.getText() != null) {
            response = aiService.sendMessage(requestDto.getChatId(), requestDto.getText());
        }
        else if(requestDto.getAudio() != null) {
            String textFromVoice = whisperService.voiceToText(requestDto.getAudio());
            response = aiService.sendMessage(requestDto.getChatId(), textFromVoice);
        }
        else {
            log.warn("Format not supported");
            return "Формат пока что не поддерживается";
        }

        return response;
    }
}
