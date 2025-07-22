package com.github.not.n0w.livepilot.aiAgent;

import com.github.not.n0w.livepilot.aiAgent.model.AiAnswer;
import com.github.not.n0w.livepilot.aiAgent.task.AiTask;

import java.util.Optional;

public interface AiAgent {
    public Optional<AiAnswer> ask(String chatId, String prompt);
    public Optional<AiAnswer> noContextAsk(String chatId, String prompt);
}
