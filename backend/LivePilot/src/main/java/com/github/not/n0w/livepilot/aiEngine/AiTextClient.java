package com.github.not.n0w.livepilot.aiEngine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.not.n0w.livepilot.aiEngine.model.AiResponse;
import com.github.not.n0w.livepilot.aiEngine.model.ChatSession;
import com.github.not.n0w.livepilot.aiEngine.model.Message;
import com.github.not.n0w.livepilot.config.AiConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AiTextClient {
    private WebClient aiWebClient;
    @Autowired
    @Qualifier("aiClient")
    public void setWebClient(WebClient webClient) {
        this.aiWebClient = webClient;
    }

    private final AiConfig aiConfig;

    public AiResponse ask(ChatSession chatSession) {

        String responseBody = aiWebClient.post()
                .bodyValue(new OpenAiApiRequest(aiConfig.getAiModel(), chatSession))
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

        return new AiResponse(assistantMessage, toolCalls);
    }
    @Getter
    static class OpenAiApiRequest {
        String model;
        List<Message> messages;
        public OpenAiApiRequest(String model, ChatSession chatSession) {
            this.model = model;
            this.messages = chatSession.getSystemMessages();
            this.messages.addAll(chatSession.getChatMessages());
        }

    }


}
