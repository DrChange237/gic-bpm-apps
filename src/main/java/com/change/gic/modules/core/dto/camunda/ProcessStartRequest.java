package com.change.gic.modules.core.dto.camunda;

import lombok.Data;

import java.util.Map;

@Data
public class ProcessStartRequest {
    private String processDefinitionKey;
    private String businessKey;
    private Map<String, Object> formData;
    private String initiatorUserId;
}
