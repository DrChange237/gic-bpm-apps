package com.change.gic.modules.core.service.faces;

import com.change.gic.modules.core.dto.camunda.ProcessStartResponse;
import com.change.gic.modules.core.dto.camunda.form.CamundaFormResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.task.Task;

import java.util.List;
import java.util.Map;

public interface CamundaService {
    boolean canUserStartProcess(String processDefinitionKey, String username);

    void claim(String taskId, String userId);

    void unclaim(String taskId);

    ProcessStartResponse startProcessWithInitiator(String processDefinitionKey,
                                                   Map<String, Object> formData,
                                                   String businessKey,
                                                   String initiatorUserId);

    List<Task> getAllTasksForUser(String userId);

    List<Task> getAllTasksForUser();

    List<Task> getActiveTasksByBusinessKey(String businessKey);

    List<ProcessDefinition> getProcessesForUser(String userId);

    CamundaFormResponseDto getStartFormByProcessKey(String processDefinitionKey);

    CamundaFormResponseDto getStartFormByProcessDefinitionId(String processDefinitionId);

    CamundaFormResponseDto getStartFormByProcessKeyAndVersion(
            String processDefinitionKey, Integer version);

    String getStartFormJsonByProcessKey(String processDefinitionKey);

    String getStartEventFormKey(String processDefinitionId);

    String prepareFormResourceName(String formKey);

    String getFormContent(String deploymentId, String formResourceName);

    CamundaFormResponseDto getTaskForm(String taskId) throws JsonProcessingException;

    Map<String, Object> getTaskFormVariables(String taskId);

    void completeTask(String taskId, Map<String, Object> variables);
}
