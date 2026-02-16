package com.change.gic.modules.core.service.faces;

import com.change.gic.modules.core.dto.camunda.CompleteTask;
import com.change.gic.modules.core.dto.camunda.TaskDto;

import java.util.List;
import java.util.Map;

public interface TaskService {
    void unClaim(String taskId);

    void claim(String taskId);

    List<TaskDto> getMyTasks();

    List<TaskDto> getTasks();

    List<TaskDto> getActiveTasksByBusinessKey(String businessKey);

    Map<String, Object> getTaskFormVariables(String taskId);

    void completeTask(CompleteTask completeTask);
}
