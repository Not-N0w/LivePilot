package com.github.not.n0w.livepilot.aiAgent.task.tasks.getMetrics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.not.n0w.livepilot.aiAgent.task.AiAgentTask;
import com.github.not.n0w.livepilot.aiAgent.PromptLoader;
import com.github.not.n0w.livepilot.aiAgent.model.AiChatSession;
import com.github.not.n0w.livepilot.aiAgent.model.Message;
import com.github.not.n0w.livepilot.aiAgent.task.AiTask;
import com.github.not.n0w.livepilot.aiAgent.tool.tools.setMetricsTool.SetMetricsTool;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class GetMetricsTask implements AiAgentTask {
    private PromptLoader promptLoader;

    @Autowired
    public void setPromptLoader(PromptLoader promptLoader) {
        this.promptLoader = promptLoader;
    }

    private Message baseSystemMessage;
    private final List<Message> subMessage = new ArrayList<>();
    private List<MetricType> metricsNeed;

    @PostConstruct
    public void init() {
        this.baseSystemMessage = new Message(
                "system",
                promptLoader.loadPromptText("taskPrompts/GetMetricsPrompt.txt")
        );
    }

    public void setMetricsNeed(MetricType... metricTypes) {
        if(metricTypes.length == 0) {
            metricsNeed = List.of(MetricType.values());
        }
        else {
            metricsNeed = List.of(metricTypes);
        }
    }

    @Override
    public AiTask getType() {
        return AiTask.GET_METRICS;
    }

    @Override
    public Message getBaseMessage() {
        return baseSystemMessage;
    }

    @Override
    public List<Message> getSubMessages() {
        return subMessage;
    }

    @Override
    public AiChatSession getAiChatSession() {
        AiChatSession request = new AiChatSession();
        request.addMessage(baseSystemMessage);
        subMessage.forEach(request::addMessage);

        setMetricsNeed();
        var tool = new SetMetricsTool(new ObjectMapper(), metricsNeed);
        request.addTool(tool);

        return request;
    }
}
