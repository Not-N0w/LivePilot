package com.github.not.n0w.livepilot.aiEngine.task;

import com.github.not.n0w.livepilot.aiEngine.model.UserSession;
import com.github.not.n0w.livepilot.model.AiTaskType;
import com.github.not.n0w.livepilot.model.User;

public interface AiTask {
    public AiTaskType getType();
    public UserSession execute(UserSession userSession, User user);

}
