package com.github.not.n0w.livepilot.aiEngine.task;

import com.github.not.n0w.livepilot.model.AiTaskType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TaskManager {
    private final Map<AiTaskType, AiTask> tasks = new HashMap<>();

    @Autowired
    public TaskManager(List<AiTask> taskList) {
        for (AiTask task : taskList) {
            tasks.put(task.getType(), task);
        }
    }

    public AiTask getTask(AiTaskType taskType) {
        return tasks.get(taskType);
    }
}
