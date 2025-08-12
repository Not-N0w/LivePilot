package com.github.not.n0w.lifepilot.model;

import lombok.Getter;

@Getter
public enum DialogStyle {
    BASE("BaseStylePrompt"),
    CASUAL_FRIEND("CasualFriendStylePrompt"),
    HYPER_HELPER("HyperHelperStylePrompt"),
    DRY_EXPERT("DryExpertStylePrompt"),
    COACH("CoachStylePrompt"),
    JOKER("JokerStylePrompt"),
    THERAPIST("TherapistStylePrompt"),
    INTERROGATOR("InterrogatorStylePrompt");

    private final String stylePromptName;

    DialogStyle(String stylePromptName) {
        this.stylePromptName = stylePromptName;
    }

}
