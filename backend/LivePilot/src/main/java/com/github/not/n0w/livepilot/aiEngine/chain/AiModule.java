package com.github.not.n0w.livepilot.aiEngine.chain;

import com.github.not.n0w.livepilot.aiEngine.model.AiResponse;
import com.github.not.n0w.livepilot.aiEngine.model.ChainRequest;
import com.github.not.n0w.livepilot.aiEngine.model.ChatSession;

public interface AiModule {
    public AiResponse passThrough(ChainRequest request);
    public boolean isTerminal();
    public void setNextAiModule(AiModule aiModule);
    public String getName();
} // gets ChatSession and returns AiResponse maybe, also can edit db
