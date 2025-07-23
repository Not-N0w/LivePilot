package com.github.not.n0w.livepilot.aiEngine;

import com.github.not.n0w.livepilot.aiEngine.chain.AiModuleChain;
import com.github.not.n0w.livepilot.aiEngine.chain.modules.TalkAiModule;
import com.github.not.n0w.livepilot.aiEngine.model.*;
import com.github.not.n0w.livepilot.aiEngine.prompt.BasePromptBuilder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AiManager {
    private final BasePromptBuilder promptBuilder;
    private final AiModuleChain chain;



    public AiResponse process(AiRequest request) {
        ChatSession chatSession = promptBuilder.getSystemMessages(request.getChat());
        chatSession.setChatMessages(request.getMessages());

        return chain.execute(
                new ChainRequest(chatSession, request.getChat().getTask(), request.getChat())
        );
    }
}
