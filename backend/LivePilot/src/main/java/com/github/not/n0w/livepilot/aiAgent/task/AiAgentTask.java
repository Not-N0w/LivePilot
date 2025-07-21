package com.github.not.n0w.livepilot.aiAgent.task;

import com.github.not.n0w.livepilot.aiAgent.model.ChatCompletionRequest;
import com.github.not.n0w.livepilot.aiAgent.model.Message;

import java.util.List;

public interface AiAgentTask {
    public Message getBaseMessage();
    public List<Message> getSubMessages();
    public AiTask getType();
    public default ChatCompletionRequest getCompletionRequest() {
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.addMessage(this.getBaseMessage());
        this.getSubMessages().forEach(request::addMessage);
        return request;
    }

}
