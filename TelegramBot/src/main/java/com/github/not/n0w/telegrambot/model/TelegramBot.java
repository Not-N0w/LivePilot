package com.github.not.n0w.telegrambot.model;

import com.github.not.n0w.telegrambot.ContentHandler;
import com.github.not.n0w.telegrambot.config.BotConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@RequiredArgsConstructor
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final ContentHandler contentHandler;

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        log.info("Message received: {}", message.getText());
        String response = contentHandler.handleIncomingMessage(message);



        try {
            execute(new SendMessage(message.getChatId().toString(), response));
        } catch (TelegramApiException e) {
            log.error("Error sending message", e);
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

}
