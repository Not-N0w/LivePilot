package com.github.not.n0w.lifepilot.aiEngine.task.tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.not.n0w.lifepilot.aiEngine.AiTextClient;
import com.github.not.n0w.lifepilot.aiEngine.model.AiResponse;
import com.github.not.n0w.lifepilot.aiEngine.model.ChatSession;
import com.github.not.n0w.lifepilot.aiEngine.model.Message;
import com.github.not.n0w.lifepilot.aiEngine.prompt.PromptLoader;
import com.github.not.n0w.lifepilot.aiEngine.task.AiTask;
import com.github.not.n0w.lifepilot.aiEngine.tool.ToolRegistry;
import com.github.not.n0w.lifepilot.model.AiTaskType;
import com.github.not.n0w.lifepilot.model.Chat;
import com.github.not.n0w.lifepilot.model.Metric;
import com.github.not.n0w.lifepilot.model.MetricType;
import com.github.not.n0w.lifepilot.repository.ChatRepository;
import com.github.not.n0w.lifepilot.repository.MetricRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class AcquaintanceTask implements AiTask {
    private final PromptLoader promptLoader;
    private final AiTextClient aiTextClient;
    private final ChatRepository chatRepository;
    private final ToolRegistry toolRegistry;
    private final MetricRepository metricRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public AiTaskType getType() {
        return AiTaskType.ACQUAINTANCE;
    }

    @Data
    static
    class UserInfo {
        private String name;
        private String gender;
    }

    private UserInfo extractUserInfoFromJson(JsonNode argumentsRaw, String chatId) {

        UserInfo userInfo = new UserInfo();
        JsonNode arguments;
        String toolName;
        try {
            arguments = argumentsRaw.get(0).get("function").get("arguments");
            toolName = argumentsRaw.get(0).get("function").get("name").asText();
        }
        catch (Exception e) {
            log.error("Wrong format of tool call");
            return userInfo;
        }
        if(!Objects.equals(toolName, "set_user_info")) {
            log.error("Wrong tool call");
            return userInfo;
        }
        try {
            if (arguments.isNull()) {
                return userInfo;
            }

            if (arguments.isTextual()) {
                arguments = objectMapper.readTree(arguments.asText());
            }

            if (!arguments.isObject()) {
                return userInfo;
            }

            ObjectNode objectNode = (ObjectNode) arguments;

            userInfo.setName(objectNode.get("name").asText());
            userInfo.setGender(objectNode.get("gender").asText());

        } catch (Exception e) {
            log.error("Failed to extract metrics from json: {}", arguments.asText());
        }

        return userInfo;
    }


    @Override
    public ChatSession execute(ChatSession chatSession, Chat chat) {
        String analyzePrompt = promptLoader.loadPromptText("taskPrompts/acquaintance/AcquaintanceAnalyzePrompt.txt");
        ChatSession analyzeMetricsChatSession = new ChatSession(
                List.of(new Message("system", analyzePrompt)),
                chatSession.getChatMessages()
        );
        AiResponse response = aiTextClient.ask(analyzeMetricsChatSession, List.of(toolRegistry.getSetUserInfoToolCall()));

        if(response.getToolCalls().isEmpty()) {
            String prompt = promptLoader.loadPromptText("taskPrompts/acquaintance/AcquaintancePrompt.txt");
            chatSession.addSystemMessage(prompt);
        }
        else {
            log.info("Tool call detected: {}", response.getToolCalls());

            chat.setTask(AiTaskType.TALK);

            UserInfo userInfo = extractUserInfoFromJson(response.getToolCalls(), chat.getId());
            chat.setName(userInfo.getName());
            chat.setGender(userInfo.getGender());

            chatRepository.save(chat);

        }
        return chatSession;
    }
}
