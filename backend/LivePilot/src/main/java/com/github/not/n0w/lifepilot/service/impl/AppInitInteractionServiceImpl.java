package com.github.not.n0w.lifepilot.service.impl;

import com.github.not.n0w.lifepilot.service.InitInteractionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppInitInteractionServiceImpl implements InitInteractionService {
    @Override
    public void pushMessage(Long userId, String message) {

    }
}
