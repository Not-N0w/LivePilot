package com.github.not.n0w.livepilot.aiEngine.chain;

import com.github.not.n0w.livepilot.aiEngine.model.AiRequest;
import com.github.not.n0w.livepilot.aiEngine.model.AiResponse;
import com.github.not.n0w.livepilot.aiEngine.model.ChainRequest;
import com.github.not.n0w.livepilot.aiEngine.model.ChatSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AiModuleChain {
    private AiModule entryAiModule;

    public void setPreviousAiModule(AiModule aiModule) {
        if(entryAiModule == null) {
            if(!aiModule.isTerminal()) {
                log.error("Last AiModule is must be terminal");
                throw new RuntimeException("Last AiModule is must be terminal");
            }
            entryAiModule = aiModule;
            return;
        }
        if(aiModule.isTerminal()) {
            log.warn("Last added AiModule is terminal. All next AiModules will be ignored");
        }
        aiModule.setNextAiModule(entryAiModule);
        entryAiModule = aiModule;
        log.info("Last added AiModule: {}", entryAiModule.getName());
    }

    public AiResponse execute(ChainRequest request) {
        return entryAiModule.passThrough(request);
    }

}
