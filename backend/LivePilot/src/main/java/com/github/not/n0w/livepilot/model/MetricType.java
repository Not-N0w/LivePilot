package com.github.not.n0w.livepilot.model;

import lombok.Getter;

@Getter
public enum MetricType {
    SLEEP("sleep"),
    ENERGY("energy"),
    NUTRITION("nutrition"),
    ACTIVITY("activity"),
    BIOMETRICS("biometrics"),
    MOOD("mood"),
    ANXIETY_CALM("anxiety_calm"),
    STRESS_RELAXATION("stress_relaxation"),
    WORK_RELATIONSHIPS("work_relationships"),
    ENVIRONMENT("environment"),
    FAMILY("family"),
    NETWORKING("networking"),
    CLARITY_OF_PLANS("clarity_of_plans"),
    GOAL_FOCUS("goal_focus"),
    GOAL_PROGRESS("goal_progress"),
    LEARNING("learning"),
    HOBBY_CREATIVITY("hobby_creativity");

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