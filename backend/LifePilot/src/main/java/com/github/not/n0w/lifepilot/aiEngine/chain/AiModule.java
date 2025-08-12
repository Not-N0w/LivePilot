package com.github.not.n0w.lifepilot.aiEngine.chain;

import com.github.not.n0w.lifepilot.aiEngine.model.AiResponse;
import com.github.not.n0w.lifepilot.aiEngine.model.ChainRequest;
import com.github.not.n0w.lifepilot.aiEngine.model.ChatSession;

public interface AiModule {
    public AiResponse passThrough(ChainRequest request);
    public boolean isTerminal();
    public void setNextAiModule(AiModule aiModule);
    public String getName();
} // gets ChatSession and returns AiResponse maybe, also can edit db
