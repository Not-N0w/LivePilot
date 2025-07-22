package com.github.not.n0w.livepilot.aiAgent.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AiAnswer {
    private final String answerToUser;
    private final JsonNode toolCalls;
}
