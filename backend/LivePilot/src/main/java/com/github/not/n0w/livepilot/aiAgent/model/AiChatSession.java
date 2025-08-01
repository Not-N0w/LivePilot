package com.github.not.n0w.livepilot.aiAgent.model;

import com.github.not.n0w.livepilot.aiAgent.tool.Tool;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiChatSession {
    private String model = "openai/gpt-4o-2024-11-20";
    private List<Message> messages = new ArrayList<>();
    @Setter
    private List<Tool> tools = new ArrayList<>();

    public AiChatSession(List<Message> messages) {
        this.messages = messages;
    }

    private void sortMessages() {
        this.messages.sort((m1, m2) -> {
            boolean isSystem1 = "system".equals(m1.getRole());
            boolean isSystem2 = "system".equals(m2.getRole());

            if (isSystem1 && !isSystem2) return -1;
            if (!isSystem1 && isSystem2) return 1;
            return 0;
        });
    }
    public void addMessage(Message message) {
        messages.add(message);
        sortMessages();
    }
    public void addChatCompletionRequest(AiChatSession aiChatSession) {
        this.tools.addAll(aiChatSession.getTools());

        if(!this.model.equals(aiChatSession.getModel())) {
            log.warn("Chat completion request models don't match");
        }
        try {
            aiChatSession.getMessages().forEach(message -> this.messages.add(1, message));
            sortMessages();
            return;
        }
        catch (Exception e) {
            log.warn("Chat completion request is empty");
        }

        messages.addAll(aiChatSession.getMessages());

    }

    public void addTool(Tool tool) {
        if (this.tools == null) {
            this.tools = new ArrayList<>();
        }
        this.tools.add(tool);
    }

}
