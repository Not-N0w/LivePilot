package com.github.not.n0w.livepilot.aiAgent.tool.tools.setMetricsTool;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.not.n0w.livepilot.aiAgent.task.AiTask;
import com.github.not.n0w.livepilot.aiAgent.task.tasks.getMetrics.MetricType;
import com.github.not.n0w.livepilot.aiAgent.tool.ToolHandler;
import com.github.not.n0w.livepilot.model.Chat;
import com.github.not.n0w.livepilot.model.Metric;
import com.github.not.n0w.livepilot.repository.ChatRepository;
import com.github.not.n0w.livepilot.repository.MetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class SetMetricsToolHandler implements ToolHandler {

    private final MetricRepository metricRepository;
    private final ChatRepository chatRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getToolName() {
        return "set_metrics";
    }

    @Override
    public void handle(JsonNode arguments, String chatId) {
        try {
            Chat chat = chatRepository.findById(chatId).get();
            chat.setCurrentTask(AiTask.TALK);
            chatRepository.save(chat);

            if (arguments == null || arguments.isNull()) {
                log.warn("Received null arguments");
                return;
            }

            if (arguments.isTextual()) {
                arguments = objectMapper.readTree(arguments.asText());
            }

            if (!arguments.isObject()) {
                log.warn("Expected JSON object, but got: {}", arguments.toPrettyString());
                return;
            }

            ObjectNode objectNode = (ObjectNode) arguments;

            objectNode.fields().forEachRemaining(entry -> {
                String metricName = entry.getKey();
                JsonNode valueNode = entry.getValue();

                if (valueNode != null && valueNode.isInt()) {
                    try {
                        MetricType metricType = MetricType.fromKey(metricName);

                        Metric metric = new Metric();
                        metric.setMetricType(metricType);
                        metric.setMetricValue(valueNode.asInt());
                        metric.setChatId(chatId);

                        metricRepository.save(metric);
                        log.info("Saved metric: {} = {}", metricName, valueNode.asInt());

                    } catch (IllegalArgumentException e) {
                        log.warn("Unknown MetricType: {}", metricName);
                    }
                } else {
                    log.debug("Skipping non-integer or null metric: {} -> {}", metricName, valueNode);
                }
            });

        } catch (Exception e) {
            log.error("Error while handling metrics", e);
        }
    }


}
