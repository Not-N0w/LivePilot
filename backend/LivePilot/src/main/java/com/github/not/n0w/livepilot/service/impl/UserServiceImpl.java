package com.github.not.n0w.livepilot.service.impl;

import com.github.not.n0w.livepilot.model.AiTaskType;
import com.github.not.n0w.livepilot.model.DialogStyle;
import com.github.not.n0w.livepilot.model.User;
import com.github.not.n0w.livepilot.repository.UserRepository;
import com.github.not.n0w.livepilot.service.UserService;
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