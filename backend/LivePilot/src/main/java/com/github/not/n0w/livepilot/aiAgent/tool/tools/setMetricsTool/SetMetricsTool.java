package com.github.not.n0w.livepilot.aiAgent.tool.tools.setMetricsTool;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.not.n0w.livepilot.aiAgent.task.tasks.getMetrics.MetricType;
import com.github.not.n0w.livepilot.aiAgent.tool.Tool;
import com.github.not.n0w.livepilot.aiAgent.tool.ToolFunction;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SetMetricsTool extends Tool {

    public SetMetricsTool(ObjectMapper objectMapper, List<MetricType> required) {
        ToolFunction function = new ToolFunction();
        function.setName("set_metrics");
        function.setDescription("Устанавливает метрики пользователя исходя  из диалога");

        try {
            String rawJson = """
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
            """;
            JsonNode baseSchema = objectMapper.readTree(rawJson);

            if (required != null && !required.isEmpty()) {
                ((com.fasterxml.jackson.databind.node.ObjectNode) baseSchema)
                        .putPOJO("required", required.stream()
                                .map(MetricType::getKey)
                                .toList());
            }

            function.setParameters(baseSchema);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse tool parameters JSON", e);
        }

        this.setType("function");
        this.setFunction(function);
    }
}
