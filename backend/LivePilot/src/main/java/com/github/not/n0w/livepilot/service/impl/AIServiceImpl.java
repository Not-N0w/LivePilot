package com.github.not.n0w.livepilot.service.impl;

import com.github.not.n0w.livepilot.aiEngine.AiManager;
import com.github.not.n0w.livepilot.aiEngine.model.AiRequest;
import com.github.not.n0w.livepilot.aiEngine.model.AiResponse;
import com.github.not.n0w.livepilot.aiEngine.model.Message;
import com.github.not.n0w.livepilot.model.AiTaskType;
import com.github.not.n0w.livepilot.model.User;
import com.github.not.n0w.livepilot.model.DialogStyle;
import com.github.not.n0w.livepilot.model.SavedMessage;
import com.github.not.n0w.livepilot.repository.UserRepository;
import com.github.not.n0w.livepilot.repository.SavedMessagesRepository;
import com.github.not.n0w.livepilot.service.AIService;
import com.github.not.n0w.livepilot.service.InitInteractionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIServiceImpl implements AIService {
    private final AiManager aiManager;
    private final UserRepository userRepository;
    private final SavedMessagesRepository savedMessagesRepository;
    private final InitInteractionService initInteractionService;

    @Override
    public void pushMessageToUser(Long userId, String message) {
        initInteractionService.pushMessage(userId, message);
    }

    @Override
    public String sendMessage(Long userId, String userText) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        List<SavedMessage> savedMessages = savedMessagesRepository.findAllByUserId(userId);
        List<Message> messageList = new java.util.ArrayList<>(savedMessages
                .stream()
                .map(msg -> new Message(msg.getRole(), msg.getMessage()))
                .toList());
        messageList.add(new Message("user", userText));

        SavedMessage userMessage = new SavedMessage();
        userMessage.setUser(user);
        userMessage.setMessage(userText);
        userMessage.setRole("user");
        savedMessagesRepository.save(userMessage);
        log.info("User message saved successfully");

        AiRequest request = new AiRequest(user, messageList);
        log.info("AiRequest: {}", request);

        AiResponse response = aiManager.process(request);
        log.info("AiResponse: {}", response);

        SavedMessage responseMessage = new SavedMessage();
        responseMessage.setUser(user);
        responseMessage.setMessage(response.getAnswerToUser());
        responseMessage.setRole("assistant");
        savedMessagesRepository.save(responseMessage);
        log.info("Assistant message saved successfully");

        return response.getAnswerToUser();
    }
}
