package com.github.not.n0w.lifepilot.aiEngine.task;

import com.github.not.n0w.lifepilot.aiEngine.model.UserSession;
import com.github.not.n0w.lifepilot.model.AiTaskType;
import com.github.not.n0w.lifepilot.model.User;

public interface AiTask {
    public AiTaskType getType();
    public UserSession execute(UserSession userSession, User user);

}
