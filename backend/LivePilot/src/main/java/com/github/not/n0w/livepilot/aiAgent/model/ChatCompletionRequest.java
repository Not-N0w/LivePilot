package com.github.not.n0w.livepilot.aiAgent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatCompletionRequest {
    private String model = "qwen/qwen3-235b-a22b";
    private List<Message> messages = new ArrayList<>();

    public ChatCompletionRequest(List<Message> messages) {
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
    public void addChatCompletionRequest(ChatCompletionRequest chatCompletionRequest) {
        if(!this.model.equals(chatCompletionRequest.getModel())) {
            log.warn("Chat completion request models don't match");
        }
        try {
            chatCompletionRequest.getMessages().forEach(message -> this.messages.add(1, message));
            sortMessages();
            return;
        }
        catch (Exception e) {
            log.warn("Chat completion request is empty");
        }

        messages.addAll(chatCompletionRequest.getMessages());

    }
}
