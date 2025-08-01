package com.github.not.n0w.livepilot.aiAgent.task;

import com.github.not.n0w.livepilot.aiAgent.model.AiChatSession;
import com.github.not.n0w.livepilot.aiAgent.model.Message;

import java.util.List;

public interface AiAgentTask {
    public Message getBaseMessage();
    public List<Message> getSubMessages();
    public AiTask getType();
    public default AiChatSession getAiChatSession() {
        AiChatSession request = new AiChatSession();
        request.addMessage(this.getBaseMessage());
        this.getSubMessages().forEach(request::addMessage);
        return request;
    }

}
