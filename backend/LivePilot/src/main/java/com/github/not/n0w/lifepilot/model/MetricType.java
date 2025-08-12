package com.github.not.n0w.lifepilot.model;

import lombok.Getter;

@Getter
public enum MetricType {
    SOCIAL_ENVIRONMENT("social_environment"),
    MENTAL_STATE("mental_state"),
    GOALS_AND_ACTIONS("goals_and_actions"),
    PHYSICAL_STATE("physical_state");
    private final String key;

    MetricType(String key) {
        this.key = key;
    }

    public static MetricType fromKey(String key) {
        for (MetricType type : values()) {
            if (type.getKey().equals(key)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No MetricType with key " + key);
    }
}