package com.change.gic.modules.core.dto.camunda;

import lombok.Data;

/**
 * DTO pour représenter une étape du processus
 */
@Data
public class ProcessStepDto {
    private String activityId;
    private String activityName;
    private String activityType;
    private java.util.Date startTime;
    private java.util.Date endTime;
    private Long durationInMillis;
    private String assignee;
    private String taskId;
    private String executionId;
}
