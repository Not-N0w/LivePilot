package com.github.not.n0w.livepilot.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.not.n0w.livepilot.dto.ChatCompletionRequest;
import com.github.not.n0w.livepilot.dto.mappers.ChatMapper;
import com.github.not.n0w.livepilot.etc.PromptLoader;
import com.github.not.n0w.livepilot.model.Chat;
import com.github.not.n0w.livepilot.model.SavedMessage;
import com.github.not.n0w.livepilot.repository.ChatRepository;
import com.github.not.n0w.livepilot.service.AIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIServiceImpl implements AIService {
    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;
    private  WebClient aiWebClient;

    @Autowired
    @Qualifier("aiClient")
    public void setWebClient(WebClient webClient) {
        this.aiWebClient = webClient;
    }

    private final PromptLoader promptLoader;

    @Override
    public String sendMessage(String chatId, String message) {
        Optional<Chat> chat = chatRepository.findByIdWithMessages(chatId);
        Chat chatToSend = chat.orElseGet(() -> {
            Chat newChat = new Chat();
            newChat.setId(chatId);
            chatRepository.save(newChat);
            return newChat;
        });

        SavedMessage userMessage = new SavedMessage();
        userMessage.setRole("user");
        userMessage.setMessage(message);
        userMessage.setChat(chatToSend);
        chatToSend.getMessages().add(userMessage);

        ChatCompletionRequest chatCompletionRequest = chatMapper.toDto(chatToSend);

        chatCompletionRequest.addSystemMessage(promptLoader.loadPrompt("static/prompts/basic"));
        //chatCompletionRequest.addSystemMessage(promptLoader.loadPrompt("static/prompts/morning_healthcheck"));

        log.info("Sending chat completion request: {}", chatCompletionRequest);
        String responseBody = aiWebClient.post()
                .bodyValue(chatCompletionRequest)
                .retrieve()
                .bodyToMono(String.class)
                .block();


        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = null;
        try {
            root = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String assistantMessage = root.path("choices").get(0).path("message").path("content").asText();

        chatRepository.saveMessage(chatId, assistantMessage, "assistant");

        return assistantMessage;
    }

}
