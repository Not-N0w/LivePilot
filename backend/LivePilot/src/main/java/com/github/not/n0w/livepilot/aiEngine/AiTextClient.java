package com.github.not.n0w.livepilot.aiEngine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.not.n0w.livepilot.aiEngine.model.AiResponse;
import com.github.not.n0w.livepilot.aiEngine.model.ChatSession;
import com.github.not.n0w.livepilot.aiEngine.model.Message;
import com.github.not.n0w.livepilot.aiEngine.tool.ToolCall;
import com.github.not.n0w.livepilot.config.AiConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.time.Duration;
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

    private AiResponse processAiResponse(String responseBody) {
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

    public AiResponse ask(ChatSession chatSession, List<ToolCall> tools) {
        OpenAiApiRequest requestPayload = new OpenAiApiRequest(
                aiConfig.getAiModel(), chatSession, tools
        );

        log.info("Request (with tools): {}", requestPayload);

        try {
            String responseBody = aiWebClient.post()
                    .bodyValue(requestPayload)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse -> {
                        log.error("AI API error: HTTP {}", clientResponse.statusCode());
                        return clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    log.error("Error body: {}", errorBody);
                                    return Mono.error(new RuntimeException("AI API error: " + errorBody));
                                });
                    })
                    .bodyToMono(String.class)
                    .retryWhen(
                            Retry.backoff(3, Duration.ofSeconds(1))
                                    .filter(throwable -> throwable instanceof IOException || throwable instanceof WebClientException)
                                    .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> retrySignal.failure())
                    )
                    .doOnError(error -> log.error("Request to AI failed", error))
                    .block();

            return processAiResponse(responseBody);

        } catch (Exception e) {
            log.error("AI communication failed: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get AI response", e);
        }
    }


    public AiResponse ask(ChatSession chatSession) {
        String requestJson = null;
        try {
            requestJson = new ObjectMapper().writeValueAsString(
                    new OpenAiApiRequest(aiConfig.getAiModel(), chatSession)
            );
        } catch (JsonProcessingException e) {
            log.error("Error parsing request: {}", requestJson);
        }

        log.info("Request JSON: {}", requestJson);

        assert requestJson != null;
        String responseBody = aiWebClient.post()
                .bodyValue(requestJson)
                .exchangeToMono(response -> {
                    log.info("Status: {}", response.statusCode());
                    return response.bodyToMono(String.class);
                })
                .block();

        log.info("Response body: {}", responseBody);

        return processAiResponse(responseBody);
    }

    @Getter
    static class OpenAiApiRequest {
        String model;
        List<Message> messages;
        List<ToolCall> tools;

        public OpenAiApiRequest(String model, ChatSession chatSession) {
            this.model = model;
            this.messages = new java.util.ArrayList<>(chatSession.getSystemMessages());
            this.messages.addAll(chatSession.getChatMessages());
        }

        public OpenAiApiRequest(String model, ChatSession chatSession, List<ToolCall> tools) {
            this.model = model;
            this.messages = new java.util.ArrayList<>(chatSession.getSystemMessages());
            this.messages.addAll(chatSession.getChatMessages());
            this.tools = tools;
        }

    }


}
