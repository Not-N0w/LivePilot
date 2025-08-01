package com.github.not.n0w.livepilot.aiAgent.task.tasks;

import com.github.not.n0w.livepilot.aiAgent.PromptLoader;
import com.github.not.n0w.livepilot.aiAgent.model.Message;
import com.github.not.n0w.livepilot.aiAgent.task.AiAgentTask;
import com.github.not.n0w.livepilot.aiAgent.task.AiTask;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TalkTask implements AiAgentTask {
    private PromptLoader promptLoader;
    @Autowired
    public void setPromptLoader(PromptLoader promptLoader) {
        this.promptLoader = promptLoader;
    }

    private Message baseSystemMessage;
    private List<Message> subMessage = new ArrayList<>();



    @PostConstruct
    public void init() {
        this.baseSystemMessage = new Message(
                "system",
                promptLoader.loadPromptText("taskPrompts/TalkPrompt")
        );
    }
    @Override
    public AiTask getType() {
        return AiTask.TALK;
    }

    @Override
    public Message getBaseMessage() {
        return baseSystemMessage;
    }

    @Override
    public List<Message> getSubMessages() {
        return subMessage;
    }
}
