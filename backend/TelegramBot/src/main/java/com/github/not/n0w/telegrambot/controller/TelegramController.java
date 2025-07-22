package com.github.not.n0w.telegrambot.controller;

import com.github.not.n0w.telegrambot.model.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class TelegramController {
    private final TelegramBot telegramBot;

    @PostMapping(value = "/send", consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    public void sendMessage(@RequestParam("chat_id") String chatId, @RequestParam("text") String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();

        try {
            telegramBot.execute(message);
        } catch (TelegramApiException e) {
            log.error("Error sending message", e);
        }
    }
}
