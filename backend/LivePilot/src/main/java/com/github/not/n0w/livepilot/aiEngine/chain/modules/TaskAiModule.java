package com.github.not.n0w.livepilot.aiEngine.chain.modules;

import com.github.not.n0w.livepilot.aiEngine.AiTextClient;
import com.github.not.n0w.livepilot.aiEngine.chain.AiModule;
import com.github.not.n0w.livepilot.aiEngine.model.AiResponse;
import com.github.not.n0w.livepilot.aiEngine.model.ChainRequest;
import com.github.not.n0w.livepilot.aiEngine.model.ChatSession;
import com.github.not.n0w.livepilot.aiEngine.model.Message;
import com.github.not.n0w.livepilot.aiEngine.task.TaskManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class TaskAiModule implements AiModule  {
    private AiModule nextAiModule;
    private final TaskManager taskManager;

    @Override
    public AiResponse passThrough(ChainRequest request) {
        var task = taskManager.getTask(request.getTaskType());
        ChatSession chatSession = task.execute(request.getChatSession(), request.getChat());
        request.setChatSession(chatSession);
        return nextAiModule.passThrough(request);
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public void setNextAiModule(AiModule aiModule) {
        nextAiModule = aiModule;
    }


    @Override
    public String getName() {
        return "TaskAiModule";
    }
}
