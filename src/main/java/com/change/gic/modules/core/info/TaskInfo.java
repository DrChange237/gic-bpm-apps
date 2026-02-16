package com.change.gic.modules.core.info;

import lombok.Data;

import java.util.Date;

@Data
public class TaskInfo {
    private String id;
    private String name;
    private String assignee;
    private Date createTime;
    private Date dueDate;
    private String processInstanceId;
    private String processDefinitionId;
    private String taskDefinitionKey;
    private int priority;
}
