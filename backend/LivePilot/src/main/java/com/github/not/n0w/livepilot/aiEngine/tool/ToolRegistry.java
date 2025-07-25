package com.github.not.n0w.livepilot.aiEngine.tool;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ToolRegistry {
    public ToolCall getSetMetricsToolCall() {
        try {
            return new ToolCall(
                    "function",
                    new ToolCall.Function(
                            "set_metrics",
                            "Extract well-being metrics from the user's chat.",
                            new ObjectMapper().readTree("""
                                        {
                                          "type": "object",
                                          "properties": {
                                            "physical_state": { "type": "integer" },
                                            "mental_state": { "type": "integer" },
                                            "social_environment": { "type": "integer" },
                                            "goals_and_actions": { "type": "integer" }
                                          }
                                        }
                                    """)
                    )
            );
        } catch (JsonProcessingException e) {
            log.info("Error parsing tool call: {}", e.getMessage());
            return null;
        }
    }

}
