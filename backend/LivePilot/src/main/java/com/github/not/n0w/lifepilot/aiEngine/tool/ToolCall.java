package com.github.not.n0w.lifepilot.aiEngine.tool;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ToolCall {
    private String type = "function";
    private Function function;

    @Data
    @AllArgsConstructor
    public static class Function {
        private String name;
        private String description;
        private JsonNode parameters;
    }
}
