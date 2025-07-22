package com.github.not.n0w.livepilot.aiAgent.tool;

import com.fasterxml.jackson.databind.JsonNode;

public interface ToolHandler {
    String getToolName();
    void handle(JsonNode arguments, String chatId);
}
