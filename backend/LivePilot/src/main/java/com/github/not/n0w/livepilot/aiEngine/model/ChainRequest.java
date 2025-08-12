package com.github.not.n0w.livepilot.aiEngine.model;

import com.github.not.n0w.livepilot.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChainRequest {
    private UserSession userSession;
    private User user;
}
