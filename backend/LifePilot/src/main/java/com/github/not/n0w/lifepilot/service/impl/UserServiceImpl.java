package com.github.not.n0w.lifepilot.service.impl;

import com.github.not.n0w.lifepilot.model.AiTaskType;
import com.github.not.n0w.lifepilot.model.DialogStyle;
import com.github.not.n0w.lifepilot.model.User;
import com.github.not.n0w.lifepilot.repository.UserRepository;
import com.github.not.n0w.lifepilot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            log.error("Username '{}' already exists", username);
            throw new AuthenticationException("User with username '" + username + "' already exists.") {};
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setUsualDialogStyle(DialogStyle.BASE);
        user.setTask(AiTaskType.ACQUAINTANCE);
        user = userRepository.save(user);
        log.info("New user created: {}", user.toString());
        return user;
    }
}