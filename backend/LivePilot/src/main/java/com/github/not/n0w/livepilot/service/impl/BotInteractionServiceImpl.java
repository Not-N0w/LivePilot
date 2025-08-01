package com.github.not.n0w.livepilot.service.impl;

import com.github.not.n0w.livepilot.config.TelegramBotConfig;
import com.github.not.n0w.livepilot.service.BotInteractionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class BotInteractionServiceImpl implements BotInteractionService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final TelegramBotConfig telegramBotConfig;

    @Override
    public void pushMessage(String chatId, String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("chat_id", chatId);
        map.add("text", message);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        String url = "http://" + telegramBotConfig.getHost() + ":" + telegramBotConfig.getPort() + "/api/send";


        try {
            restTemplate.postForObject(
                    url,
                    request,
                    String.class
            );

            log.info("Sent message to chat {}", chatId);
        } catch (Exception e) {
            log.error("Failed to send message to chat {}: {}", chatId, e.getMessage());
        }
    }
}
