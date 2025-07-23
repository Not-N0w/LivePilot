package com.github.not.n0w.livepilot.config;

import com.github.not.n0w.livepilot.aiEngine.chain.AiModuleChain;
import com.github.not.n0w.livepilot.aiEngine.chain.modules.TalkAiModule;
import com.github.not.n0w.livepilot.aiEngine.chain.modules.TaskAiModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiModuleConfig {

    @Bean
    public AiModuleChain aiModuleChain(TalkAiModule talkAiModule, TaskAiModule taskAiModule) {
        AiModuleChain chain = new AiModuleChain();

        chain.setPreviousAiModule(talkAiModule);
        chain.setPreviousAiModule(taskAiModule);

        return chain;
    }
}
