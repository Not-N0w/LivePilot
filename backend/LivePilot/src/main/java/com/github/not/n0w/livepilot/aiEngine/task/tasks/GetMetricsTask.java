package com.github.not.n0w.livepilot.aiEngine.task.tasks;

import com.github.not.n0w.livepilot.aiEngine.AiTextClient;
import com.github.not.n0w.livepilot.aiEngine.model.AiResponse;
import com.github.not.n0w.livepilot.aiEngine.model.ChatSession;
import com.github.not.n0w.livepilot.aiEngine.model.Message;
import com.github.not.n0w.livepilot.aiEngine.prompt.PromptLoader;
import com.github.not.n0w.livepilot.aiEngine.task.AiTask;
import com.github.not.n0w.livepilot.model.AiTaskType;
import com.github.not.n0w.livepilot.model.Chat;
import com.github.not.n0w.livepilot.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetMetricsTask implements AiTask {
    private final PromptLoader promptLoader;
    private final AiTextClient aiTextClient;
    private final ChatRepository chatRepository;

    @Override
    public AiTaskType getType() {
        return AiTaskType.GET_METRICS;
    }

    @Override
    public ChatSession execute(ChatSession chatSession, Chat chat) {
        String analyzePrompt = promptLoader.loadPromptText("taskPrompts/getMetrics/GetMetricsAnalyzePrompt.txt");
        ChatSession analyzeMetricsChatSession = new ChatSession(
                List.of(new Message("system", analyzePrompt)),
                chatSession.getChatMessages()
        );
        AiResponse response = aiTextClient.ask(analyzeMetricsChatSession); //todo add tool call

        if(response.getToolCalls().isEmpty()) {
            String prompt = promptLoader.loadPromptText("taskPrompts/getMetrics/GetMetricsPrompt.txt");
            chatSession.addSystemMessage(prompt);
        }
        else {
            log.info("Tool call detected: {}", response.getToolCalls());
            chat.setTask(AiTaskType.TALK);
            chatRepository.save(chat);
            String prompt = promptLoader.loadPromptText("taskPrompts/getMetrics/GetMetricsAcceptPrompt.txt");
            chatSession.addSystemMessage(prompt);
        }
        return chatSession;
    }
}
