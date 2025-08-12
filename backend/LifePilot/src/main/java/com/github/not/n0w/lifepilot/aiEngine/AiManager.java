package com.github.not.n0w.lifepilot.aiEngine;

import com.github.not.n0w.lifepilot.aiEngine.chain.AiModuleChain;
import com.github.not.n0w.lifepilot.aiEngine.model.*;
import com.github.not.n0w.lifepilot.aiEngine.prompt.BasePromptBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AiManager {
    private final BasePromptBuilder promptBuilder;
    private final AiModuleChain chain;



    public AiResponse process(AiRequest request) {
        UserSession userSession = promptBuilder.getSystemMessages(request.getUser());
        userSession.setUserMessages(request.getMessages());

        return chain.execute(
                new ChainRequest(userSession, request.getUser())
        );
    }
}
