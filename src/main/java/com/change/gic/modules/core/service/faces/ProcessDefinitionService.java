package com.change.gic.modules.core.service.faces;

import com.change.gic.modules.core.dto.camunda.ProcessDefinitionDto;

import java.util.List;

public interface ProcessDefinitionService {
    List<ProcessDefinitionDto> getProcessDefinitions();
}
