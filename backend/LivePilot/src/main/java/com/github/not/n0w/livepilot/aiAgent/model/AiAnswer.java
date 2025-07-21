package com.github.not.n0w.livepilot.aiAgent.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AiAnswer {
    private final String answerToUser;
    // todo tool answer
}
