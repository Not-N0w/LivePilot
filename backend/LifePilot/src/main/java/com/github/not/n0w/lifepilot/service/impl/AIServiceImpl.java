package com.github.not.n0w.lifepilot.service.impl;

import com.github.not.n0w.lifepilot.aiEngine.AiManager;
import com.github.not.n0w.lifepilot.aiEngine.chain.AiModuleChain;
import com.github.not.n0w.lifepilot.aiEngine.model.AiRequest;
import com.github.not.n0w.lifepilot.aiEngine.model.AiResponse;
import com.github.not.n0w.lifepilot.aiEngine.model.Message;
import com.github.not.n0w.lifepilot.model.AiTaskType;
import com.github.not.n0w.lifepilot.model.Chat;
import com.github.not.n0w.lifepilot.model.DialogStyle;
import com.github.not.n0w.lifepilot.model.SavedMessage;
import com.github.not.n0w.lifepilot.repository.ChatRepository;
import com.github.not.n0w.lifepilot.repository.SavedMessagesRepository;
import com.github.not.n0w.lifepilot.service.AIService;
import com.github.not.n0w.lifepilot.service.BotInteractionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIServiceImpl implements AIService {
    private final AiManager aiManager;
    private final ChatRepository chatRepository;
    private final SavedMessagesRepository savedMessagesRepository;
    private final BotInteractionService botInteractionService;

    @Override
    public void pushMessageToUser(String chatId, String message) {
        botInteractionService.pushMessage(chatId, message);
    }

    @Override
    public String sendMessage(String chatId, String userText) {
        Chat chat = chatRepository.findById(chatId)
                .orElseGet(() -> {
                    Chat newChat = new Chat();
                    newChat.setId(chatId);
                    newChat.setUsualDialogStyle(DialogStyle.BASE);
                    newChat.setTask(AiTaskType.ACQUAINTANCE);
                    chatRepository.save(newChat);
                    log.info("New chat created: {}", newChat);
                    return newChat;
                });

        List<SavedMessage> savedMessages = savedMessagesRepository.findAllByChatId(chatId);
        List<Message> messageList = new java.util.ArrayList<>(savedMessages
                .stream()
                .map(msg -> new Message(msg.getRole(), msg.getMessage()))
                .toList());
        messageList.add(new Message("user", userText));

        SavedMessage userMessage = new SavedMessage();
        userMessage.setChat(chat);
        userMessage.setMessage(userText);
        userMessage.setRole("user");
        savedMessagesRepository.save(userMessage);
        log.info("User message saved successfully");

        AiRequest request = new AiRequest(chat, messageList);
        log.info("AiRequest: {}", request);

        AiResponse response = aiManager.process(request);
        log.info("AiResponse: {}", response);

        SavedMessage responseMessage = new SavedMessage();
        responseMessage.setChat(chat);
        responseMessage.setMessage(response.getAnswerToUser());
        responseMessage.setRole("assistant");
        savedMessagesRepository.save(responseMessage);
        log.info("Assistant message saved successfully");

        return response.getAnswerToUser();
    }
}
