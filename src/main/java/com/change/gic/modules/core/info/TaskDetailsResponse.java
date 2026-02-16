package com.change.gic.modules.core.info;

import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
public class TaskDetailsResponse {
    private TaskInfo task;
    private ProcessDefinitionInfo processDefinition;
    private Map<String, Object> processVariables;
    private List<ActivityHistoryInfo> activityHistory;
    private ActivityUserTaskInfo activityUserTask;
    private List<DocumentInfo> documents;
}
