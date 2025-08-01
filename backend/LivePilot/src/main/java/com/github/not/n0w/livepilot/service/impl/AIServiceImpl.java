package com.github.not.n0w.livepilot.service.impl;

import com.github.not.n0w.livepilot.aiAgent.AiAgent;
import com.github.not.n0w.livepilot.aiAgent.task.AiTask;
import com.github.not.n0w.livepilot.service.AIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIServiceImpl implements AIService {
    private final AiAgent aiAgent;

    @Override
    public String sendMessage(String chatId, String userText) {
        var answer = aiAgent.ask(chatId, userText);
        if(answer.isPresent()) {
            return answer.get().getAnswerToUser();
        }
        return "Answer error";
    }


}
