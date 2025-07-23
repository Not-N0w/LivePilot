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
                                            "sleep": { "type": "integer" },
                                            "energy": { "type": "integer" },
                                            "nutrition": { "type": "integer" },
                                            "activity": { "type": "integer" },
                                            "biometrics": { "type": "integer" },
                                            "mood": { "type": "integer" },
                                            "anxiety_calm": { "type": "integer" },
                                            "stress_relaxation": { "type": "integer" },
                                            "work_relationships": { "type": "integer" },
                                            "environment": { "type": "integer" },
                                            "family": { "type": "integer" },
                                            "networking": { "type": "integer" },
                                            "clarity_of_plans": { "type": "integer" },
                                            "goal_focus": { "type": "integer" },
                                            "goal_progress": { "type": "integer" },
                                            "learning": { "type": "integer" },
                                            "hobby_creativity": { "type": "integer" }
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
