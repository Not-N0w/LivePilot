package com.github.not.n0w.livepilot.aiEngine.task.tasks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.not.n0w.livepilot.aiEngine.AiTextClient;
import com.github.not.n0w.livepilot.aiEngine.model.AiResponse;
import com.github.not.n0w.livepilot.aiEngine.model.ChatSession;
import com.github.not.n0w.livepilot.aiEngine.model.Message;
import com.github.not.n0w.livepilot.aiEngine.prompt.PromptLoader;
import com.github.not.n0w.livepilot.aiEngine.task.AiTask;
import com.github.not.n0w.livepilot.aiEngine.tool.ToolRegistry;
import com.github.not.n0w.livepilot.model.AiTaskType;
import com.github.not.n0w.livepilot.model.Chat;
import com.github.not.n0w.livepilot.model.Metric;
import com.github.not.n0w.livepilot.model.MetricType;
import com.github.not.n0w.livepilot.repository.ChatRepository;
import com.github.not.n0w.livepilot.repository.MetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetMetricsTask implements AiTask {
    private final PromptLoader promptLoader;
    private final AiTextClient aiTextClient;
    private final ChatRepository chatRepository;
    private final ToolRegistry toolRegistry;
    private final MetricRepository metricRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public AiTaskType getType() {
        return AiTaskType.GET_METRICS;
    }

    public List<Metric> extractMetricsFromJson(JsonNode argumentsRaw, String chatId) {
        List<Metric> metrics = new ArrayList<>();
        JsonNode arguments;
        String toolName;
        try {
            arguments = argumentsRaw.get(0).get("function").get("arguments");
            toolName = argumentsRaw.get(0).get("function").get("name").asText();
        }
        catch (Exception e) {
            log.error("Wrong format of tool call");
            return metrics;
        }
        if(!Objects.equals(toolName, "set_metrics")) {
            log.error("Wrong tool call");
            return metrics;
        }
        try {
            if (arguments.isNull()) {
                return metrics;
            }

            if (arguments.isTextual()) {
                arguments = objectMapper.readTree(arguments.asText());
            }

            if (!arguments.isObject()) {
                return metrics;
            }

            ObjectNode objectNode = (ObjectNode) arguments;
            Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();

            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String metricName = entry.getKey();
                JsonNode valueNode = entry.getValue();

                if (valueNode != null && valueNode.isInt()) {
                    try {
                        MetricType metricType = MetricType.fromKey(metricName);

                        Metric metric = new Metric();
                        metric.setMetricType(metricType);
                        metric.setMetricValue(valueNode.asInt());
                        metric.setChatId(chatId);

                        metrics.add(metric);
                    } catch (IllegalArgumentException e) {
                        log.warn("Unknown metric type: {}", metricName);
                    }
                }
            }

        } catch (Exception e) {
            log.error("Failed to extract metrics from json: {}", arguments.asText());
        }

        return metrics;
    }

    @Override
    public ChatSession execute(ChatSession chatSession, Chat chat) {
        AiResponse response = null;
        if(chat.getExtraState() == 0) {
            String analyzePrompt = promptLoader.loadPromptText("taskPrompts/getMetrics/GetMetricsAnalyzePrompt.txt");
            ChatSession analyzeMetricsChatSession = new ChatSession(
                    List.of(new Message("system", analyzePrompt)),
                    chatSession.getChatMessages()
            );
           response = aiTextClient.ask(analyzeMetricsChatSession, List.of(toolRegistry.getSetMetricsToolCall()));
        }
        boolean isNoToolCalls = response == null || response.getToolCalls().isEmpty();
        if(isNoToolCalls) {
            String prompt;
            if(chat.getExtraState() == 2) {
                prompt = promptLoader.loadPromptText("taskPrompts/getMetrics/GetMetricsRetryPrompt.txt");

            }
            else {
                prompt = promptLoader.loadPromptText("taskPrompts/getMetrics/GetMetricsPrompt.txt");
            }
            chatSession.addSystemMessage(prompt);
            chatSession.setChatMessages(new ArrayList<>());
        }
        else {
            log.info("Tool call detected: {}", response.getToolCalls());

            chat.setTask(AiTaskType.TALK);

            List<Metric> previousMetrics = metricRepository.findLatestMetricsByChatId(chat.getId());


            String metricsStr = "Прошлые метрики пользователя: " +
                    (previousMetrics == null ? "" : previousMetrics.toString()) + '\n';

            chatRepository.save(chat);
            List<Metric> metrics = extractMetricsFromJson(response.getToolCalls(), chat.getId());
            metricRepository.saveAll(metrics);
            log.info("Metrics saved to database");

            String prompt = promptLoader.loadPromptText("taskPrompts/getMetrics/GetMetricsAcceptPrompt.txt");
            chatSession.addSystemMessage(prompt);


            metricsStr += "Новые метрики пользователя:\n" +  metrics.toString();
            chatSession.addSystemMessage(metricsStr);
        }
        return chatSession;
    }
}
