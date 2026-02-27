package com.change.gic.modules.core.service.impl;


import com.change.gic.modules.core.dto.camunda.CompleteTask;
import com.change.gic.modules.core.dto.camunda.TaskDto;
import com.change.gic.modules.core.service.faces.AuthService;
import com.change.gic.modules.core.service.faces.CamundaService;
import com.change.gic.modules.core.service.faces.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final CamundaService camundaService;
    private final AuthService authService;
    private final RuntimeService runtimeService;
    private final RepositoryService repositoryService;


    @Override
    public void unClaim(String taskId) {
        String username = authService.getCurrentUsername();
        log.info("getMyTasks username={}", username);
        camundaService.unclaim(taskId);
    }

    @Override
    public void claim(String taskId) {
        String username = authService.getCurrentUsername();
        log.info("getMyTasks username={}", username);
        camundaService.claim(taskId, username);
    }

    @Override
    public List<TaskDto> getMyTasks() {
        String username = authService.getCurrentUsername();
        log.info("getMyTasks username={}", username);
       List<Task> allTasks = camundaService.getAllTasksForUser(username);
        return allTasks.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDto> getTasks() {
        List<Task> allTasks = camundaService.getAllTasksForUser();
        return allTasks.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDto> getActiveTasksByBusinessKey(String businessKey) {
        List<Task> allTasks = camundaService.getActiveTasksByBusinessKey(businessKey);
        return allTasks.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    @Override
    public Map<String, Object> getTaskFormVariables(String taskId) {
        return camundaService.getTaskFormVariables(taskId);
    }


    @Override
    public void completeTask(CompleteTask completeTask) {
        String username = authService.getCurrentUsername();
        log.info("getMyTasks username={}", username);
        camundaService.completeTask(completeTask.getTaskId(), completeTask.getFormData());
    }

    @Override
    public void taskComplete(String taskId, MultipartHttpServletRequest request) {

        Map<String, Object> formData = new HashMap<>();

        // 1️⃣ Paramètres texte avec conversion automatique
        request.getParameterMap().forEach((key, values) -> {
            String value = values[0];

            if (value == null || value.isBlank()) {
                formData.put(key, null);
                return;
            }

            formData.put(key, convertValue(value));
        });

        // 2️⃣ Fichiers
        request.getFileMap().forEach((key, file) -> {
            if (!file.isEmpty()) {
                formData.put(key, file);
            }
        });

        log.info(formData.toString());
        log.info(taskId);

        String username = authService.getCurrentUsername();
        log.info("getMyTasks username={}", username);
        camundaService.completeTask(taskId, formData);
    }

    private Object convertValue(String value) {

        // Boolean
        if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
            return Boolean.parseBoolean(value);
        }

        // Integer
        if (value.matches("^-?\\d+$")) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return Long.parseLong(value);
            }
        }

        // Decimal
        if (value.matches("^-?\\d+\\.\\d+$")) {
            return new BigDecimal(value);
        }

        // Date ISO (yyyy-MM-dd)
        try {
            return LocalDate.parse(value, DateTimeFormatter.ISO_DATE);
        } catch (Exception ignored) {}

        // Sinon String
        return value;
    }

    /**
     * Convertit une Task Camunda en DTO
     */
    private TaskDto mapToDto(Task task) {

        // Process Instance
        ProcessInstance processInstance = runtimeService
                .createProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .singleResult();

        // Process Definition
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionId(task.getProcessDefinitionId())
                .singleResult();

        return TaskDto.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .assignee(task.getAssignee())
                .createTime(task.getCreateTime())
                .dueDate(task.getDueDate())
                .followUpDate(task.getFollowUpDate())
                .priority(task.getPriority())
                .processInstanceId(task.getProcessInstanceId())
                .processDefinitionId(task.getProcessDefinitionId())
                .taskDefinitionKey(task.getTaskDefinitionKey())
                .suspended(task.isSuspended())
                .businessKey(processInstance != null ? processInstance.getBusinessKey() : null)
                .processDefinitionName(processDefinition != null ? processDefinition.getName() : null)
                .build();
    }

}
