package com.github.not.n0w.livepilot.aiAgent.tool;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToolFunction {
    private String name;
    private String description;
    private JsonNode parameters;
}