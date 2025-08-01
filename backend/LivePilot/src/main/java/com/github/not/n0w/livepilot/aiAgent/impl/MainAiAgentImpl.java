package com.github.not.n0w.livepilot.aiAgent.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.not.n0w.livepilot.aiAgent.AiAgent;
import com.github.not.n0w.livepilot.aiAgent.PromptLoader;
import com.github.not.n0w.livepilot.aiAgent.model.AiAnswer;
import com.github.not.n0w.livepilot.aiAgent.model.AiChatSession;
import com.github.not.n0w.livepilot.aiAgent.model.Message;
import com.github.not.n0w.livepilot.aiAgent.model.mapper.ChatMapper;
import com.github.not.n0w.livepilot.aiAgent.task.AiAgentTask;
import com.github.not.n0w.livepilot.aiAgent.task.AiTask;
import com.github.not.n0w.livepilot.aiAgent.task.tasks.TaskRegistry;
import com.github.not.n0w.livepilot.aiAgent.tool.ToolExecutor;
import com.github.not.n0w.livepilot.model.Chat;
import com.github.not.n0w.livepilot.model.SavedMessage;
import com.github.not.n0w.livepilot.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MainAiAgentImpl implements AiAgent {

    private final PromptLoader promptLoader;
    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;
    private final TaskRegistry taskRegistry;

    private WebClient aiWebClient;
    private final AiChatSession baseCompletionRequest;
    private final ToolExecutor toolExecutor;

    @Autowired
    public MainAiAgentImpl(PromptLoader promptLoader,
                           ChatRepository chatRepository,
                           ChatMapper chatMapper,
                           TaskRegistry taskRegistry, ToolExecutor toolExecutor) {
        this.promptLoader = promptLoader;
        this.chatRepository = chatRepository;
        this.chatMapper = chatMapper;
        this.taskRegistry = taskRegistry;

        this.baseCompletionRequest = new AiChatSession(
                List.of(new Message("system", promptLoader.loadPromptText("BasePrompt.txt")))
        );
        this.toolExecutor = toolExecutor;
    }

    @Autowired
    @Qualifier("aiClient")
    public void setWebClient(WebClient webClient) {
        this.aiWebClient = webClient;
    }


    @Override
    public Optional<AiAnswer> noContextAsk(String chatId, String prompt) {
        Chat chat = chatRepository.findByIdWithMessages(chatId)
                .orElseGet(() -> {
                    Chat newChat = new Chat();
                    newChat.setId(chatId);
                    chatRepository.save(newChat);
                    return newChat;
                });
        AiTask task = chat.getCurrentTask();

        var currentTask = taskRegistry.getTask(task);

        if (currentTask == null) {
            log.warn("Task not found: {}", task);
            return Optional.empty();
        }
        AiChatSession taskCompletionRequest = currentTask.getAiChatSession();

        AiChatSession aiChatSession = new AiChatSession();
        aiChatSession.addChatCompletionRequest(this.baseCompletionRequest);
        aiChatSession.addChatCompletionRequest(taskCompletionRequest);

        AiAnswer answer = requestToGpt(aiChatSession);

        chatRepository.saveMessage(chatId, answer.getAnswerToUser(), "assistant");

        return Optional.of(answer);
    }

    private AiAnswer requestToGpt(AiChatSession completionRequest) {
        log.info("Sending chat completion request: {}", completionRequest);

        String responseBody = aiWebClient.post()
                .bodyValue(completionRequest)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JsonNode root;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            root = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            log.error("Error parsing response: {}", responseBody);
            throw new RuntimeException(e);
        }

        log.info("Response: {}", root);

        JsonNode messageNode = root.path("choices").path(0).path("message");
        String assistantMessage = messageNode.path("content").asText(null);
        JsonNode toolCalls = messageNode.path("tool_calls");

        return new AiAnswer(assistantMessage, toolCalls);
    }

    private Optional<AiAnswer> getAiAnswer(String chatId, Chat chat, AiAgentTask currentTask) {
        chatRepository.save(chat);

        AiChatSession aiChatSession = chatMapper.toDto(chat);
        aiChatSession.addChatCompletionRequest(this.baseCompletionRequest);
        aiChatSession.addChatCompletionRequest(currentTask.getAiChatSession());

        AiAnswer answer = requestToGpt(aiChatSession);
        toolExecutor.executeTool(answer, chatId);
        chatRepository.saveMessage(chatId, answer.getAnswerToUser(), "assistant");

        return Optional.of(answer);
    }

    @Override
    public Optional<AiAnswer> ask(String chatId, String userText) {

        Chat chat = chatRepository.findByIdWithMessages(chatId)
                .orElseGet(() -> {
                    Chat newChat = new Chat();
                    newChat.setId(chatId);
                    chatRepository.save(newChat);
                    return newChat;
                });
        AiTask task = chat.getCurrentTask();

        var currentTask = taskRegistry.getTask(task);

        if (currentTask == null) {
            log.warn("Task not found: {}", task);
            return Optional.empty();
        }

        SavedMessage userMessage = new SavedMessage();
        userMessage.setRole("user");
        userMessage.setMessage(userText);
        userMessage.setChat(chat);
        chat.getMessages().add(userMessage);

        return getAiAnswer(chatId, chat, currentTask);
    }
}
