package com.github.not.n0w.telegrambot;


import com.github.not.n0w.telegrambot.dto.RequestDto;
import com.github.not.n0w.telegrambot.dto.ResponseDto;

import com.github.not.n0w.telegrambot.service.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.api.objects.Message;


// todo add support of sending images and voices as bot answers
@Component
@RequiredArgsConstructor
public class ContentHandler {
    private final Transmitter transmitter;
    private final TelegramService telegramService;


    public String handleIncomingMessage(Message message) {
        if (message == null) {
            return "";
        }

        if (message.hasText()) {
            return handleTextMessage(message);
        } else if (message.hasVoice()) {
            return handleAudioMessage(message);
        }

        return "Unsupported message type";
    }

    private String handleTextMessage(Message message) {
        ResponseDto responseDto = transmitter.sendToServer(
                RequestDto.builder()
                        .chatId(message.getChatId().toString())
                        .text(message.getText())
                        .build()
        );

        return responseDto.getText();
    }

    private String handleAudioMessage(Message message) {
        byte[] fileBytes = telegramService.downloadByFileId(message.getVoice().getFileId());

        ResponseDto responseDto = transmitter.sendToServer(
                RequestDto.builder()
                        .chatId(message.getChatId().toString())
                        .audio(fileBytes)
                        .build()
        );

        return responseDto.getText();
    }
}
