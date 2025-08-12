package com.github.not.n0w.lifepilot.aiEngine.model;

import com.github.not.n0w.lifepilot.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChainRequest {
    private UserSession userSession;
    private User user;
}
