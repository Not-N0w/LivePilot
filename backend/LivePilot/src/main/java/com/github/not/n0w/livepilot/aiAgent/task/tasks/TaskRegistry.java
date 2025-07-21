package com.github.not.n0w.livepilot.aiAgent.task.tasks;

import com.github.not.n0w.livepilot.aiAgent.task.AiAgentTask;
import com.github.not.n0w.livepilot.aiAgent.task.AiTask;
import com.github.not.n0w.livepilot.aiAgent.task.tasks.getMetrics.GetMetricsTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TaskRegistry {
    private final Map<AiTask, AiAgentTask> tasks = new HashMap<>();

    @Autowired
    public TaskRegistry(List<AiAgentTask> taskList) {
        for (AiAgentTask task : taskList) {
            tasks.put(task.getType(), task);
        }
    }

    public AiAgentTask getTask(AiTask task) {
        return tasks.get(task);
    }
}
