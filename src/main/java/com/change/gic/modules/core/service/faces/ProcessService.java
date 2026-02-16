package com.change.gic.modules.core.service.faces;

import com.change.gic.modules.core.dto.camunda.ProcessStartResponse;
import com.change.gic.modules.core.info.ProcessInfo;

import java.util.List;
import java.util.Map;

public interface ProcessService {

    ProcessStartResponse startProcessWithInitiator(String processDefinitionKey,
                                                   Map<String, Object> formData,
                                                   String businessKey);

    List<ProcessInfo> processListByModule(String moduleId);

    List<ProcessInfo> processList();
}
