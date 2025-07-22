package com.github.not.n0w.livepilot.aiAgent.tool;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.not.n0w.livepilot.aiAgent.AiAgent;
import com.github.not.n0w.livepilot.aiAgent.model.AiAnswer;
import com.github.not.n0w.livepilot.aiAgent.task.AiAgentTask;
import com.github.not.n0w.livepilot.repository.MetricRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ToolExecutor {

    private final List<ToolHandler> handlers;

    public void executeTool(AiAnswer aiAnswer, String chatId) {
        JsonNode toolCalls = aiAnswer.getToolCalls();

        if (toolCalls == null || !toolCalls.isArray()) return;

        for (JsonNode call : toolCalls) {
            JsonNode function = call.get("function");
            if (function == null) continue;

            String name = function.get("name").asText();
            JsonNode arguments = function.get("arguments");

            ToolHandler handler = handlers.stream()
                    .filter(h -> h.getToolName().equals(name))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No handler for tool: " + name));

            handler.handle(arguments, chatId);
        }
    }
}
