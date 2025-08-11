package com.github.not.n0w.telegrambot.model;

import com.github.not.n0w.telegrambot.ContentHandler;
import com.github.not.n0w.telegrambot.config.BotConfig;
import com.github.not.n0w.telegrambot.utils.MarkdownUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
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
        Long chatId = message.getChatId();

        log.info("Message received: {}", message.getText());

        try {
            execute(SendChatAction.builder()
                    .chatId(chatId.toString())
                    .action("typing")
                    .build());

            Thread.sleep(500);

            String response = MarkdownUtil.escapeMarkdownV2(
                    contentHandler.handleIncomingMessage(message)
            );

            execute(
                    SendMessage.builder()
                    .chatId(chatId.toString())
                    .text(response)
                    .parseMode("MarkdownV2")
                    .build()
            );

        } catch (TelegramApiException | InterruptedException e) {
            log.error("Error in bot processing", e);
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
