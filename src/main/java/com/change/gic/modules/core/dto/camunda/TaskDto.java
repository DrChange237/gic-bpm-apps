package com.change.gic.modules.core.dto.camunda;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskDto {
    private String id;
    private String name;
    private String description;
    private String assignee;
    private java.util.Date createTime;
    private java.util.Date dueDate;
    private java.util.Date followUpDate;
    private int priority;
    private String processInstanceId;
    private String processDefinitionId;
    private String taskDefinitionKey;
    private boolean suspended;
    private String processDefinitionName;
    private String businessKey;
    private String customerName;
}
