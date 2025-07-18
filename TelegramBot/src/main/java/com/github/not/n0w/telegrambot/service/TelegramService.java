package com.github.not.n0w.telegrambot.service;

import com.github.not.n0w.telegrambot.config.BotConfig;
import com.github.not.n0w.telegrambot.model.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramService {
    private final BotConfig botConfig;
    private TelegramBot telegramBot;
    @Autowired
    public void setTelegramBot(@Lazy TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }
    public byte[] downloadByFileId(String fileId) {
        try {
            GetFile getFileMethod = new GetFile();
            getFileMethod.setFileId(fileId);
            File file = telegramBot.execute(getFileMethod);
            String filePath = file.getFilePath();

            try (InputStream in = new URL("https://api.telegram.org/file/bot" + botConfig.getToken() + "/" + filePath).openStream()) {
                return in.readAllBytes();
            }

        } catch (Exception e) {
            log.error("Error downloading file", e);
            throw new RuntimeException("Failed to download file", e);
        }
    }

}
