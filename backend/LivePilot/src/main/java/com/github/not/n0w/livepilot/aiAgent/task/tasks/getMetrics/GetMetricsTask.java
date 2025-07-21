package com.github.not.n0w.livepilot.aiAgent.task.tasks.getMetrics;

import com.github.not.n0w.livepilot.aiAgent.task.AiAgentTask;
import com.github.not.n0w.livepilot.aiAgent.PromptLoader;
import com.github.not.n0w.livepilot.aiAgent.model.ChatCompletionRequest;
import com.github.not.n0w.livepilot.aiAgent.model.Message;
import com.github.not.n0w.livepilot.aiAgent.task.AiTask;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final Map<Metric, Boolean> metrics_need = new HashMap<>();

    @PostConstruct
    public void init() {
        this.baseSystemMessage = new Message(
                "system",
                promptLoader.loadPromptText("taskPrompts/GetMetricsPrompt")
        );
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
    public ChatCompletionRequest getCompletionRequest() {
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.addMessage(baseSystemMessage);
        subMessage.forEach(request::addMessage);
        return request;
    }
}
