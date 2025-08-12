package com.github.not.n0w.lifepilot.service;

import com.github.not.n0w.lifepilot.model.User;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public User registerUser(String username, String password) throws AuthenticationException;
}
