package com.change.gic.modules.core.service.impl;

import com.change.gic.modules.core.dto.camunda.ProcessDefinitionDto;
import com.change.gic.modules.core.service.faces.AuthService;
import com.change.gic.modules.core.service.faces.CamundaService;
import com.change.gic.modules.core.service.faces.ProcessDefinitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProcessDefinitionServiceImpl implements ProcessDefinitionService {

    private final CamundaService camundaService;
    private final AuthService authService;

    @Override
    public List<ProcessDefinitionDto> getProcessDefinitions() {
        String username = authService.getCurrentUsername();
        List<ProcessDefinition> processDefinitions = camundaService.getProcessesForUser(username);
        return processDefinitions.stream().map(pd -> new ProcessDefinitionDto(
                pd.getId(),
                pd.getKey(),
                pd.getName(),
                pd.getDescription(),
                pd.getCategory()
        )).collect(Collectors.toList());
    }

}
