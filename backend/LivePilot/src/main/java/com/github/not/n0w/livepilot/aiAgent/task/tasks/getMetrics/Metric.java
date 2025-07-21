package com.github.not.n0w.livepilot.aiAgent.task.tasks.getMetrics;

public enum Metric {
    SLEEP_QUALITY("sleep_quality"),
    ENERGY_LEVEL("energy_level"),
    PHYSICAL_ACTIVITY("physical_activity"),
    STRESS_LEVEL("stress_level"),
    EMOTIONAL_STATE("emotional_state"),
    FOCUS_LEVEL("focus_level"),
    COMMUNICATION_QUALITY("communication_quality"),
    SOCIAL_CONNECTION("social_connection"),
    GOAL_PROGRESS("goal_progress"),
    LEARNING_ENGAGEMENT("learning_engagement");

    private final String key;

    Metric(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}