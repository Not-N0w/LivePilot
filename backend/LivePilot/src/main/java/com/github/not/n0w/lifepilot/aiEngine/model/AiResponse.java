package com.github.not.n0w.lifepilot.aiEngine.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AiResponse {
    private final String answerToUser;
    private final JsonNode toolCalls;
}