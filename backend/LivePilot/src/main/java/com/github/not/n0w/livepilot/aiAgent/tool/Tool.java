package com.github.not.n0w.livepilot.aiAgent.tool;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tool {
    private String type = "function";
    private ToolFunction function;
}