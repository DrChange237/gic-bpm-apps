package com.change.gic.modules.core.dto.camunda;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProcessStartResponse {
    private boolean success;
    private String processInstanceId;
    private String businessKey;
    private String message;
}
