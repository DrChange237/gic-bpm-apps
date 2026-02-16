package com.change.gic.modules.core.dto.camunda;

import lombok.Data;

import java.util.Map;

@Data
public class CompleteTask {
    private Map<String, Object> formData;
    private String taskId;
}
