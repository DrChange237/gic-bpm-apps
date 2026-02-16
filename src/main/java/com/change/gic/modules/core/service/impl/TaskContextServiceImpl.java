package com.change.gic.modules.core.service.impl;

import com.change.gic.modules.core.entity.ActivityUserTask;
import com.change.gic.modules.core.entity.Document;
import com.change.gic.modules.core.info.*;
import com.change.gic.modules.core.mappers.ActivityUserTaskMapper;
import com.change.gic.modules.core.mappers.DocumentMapper;
import com.change.gic.modules.core.repository.ActivityUserTaskRepository;
import com.change.gic.modules.core.repository.DocumentRepository;
import com.change.gic.modules.core.service.faces.TaskContextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskContextServiceImpl implements TaskContextService {

    private final TaskService taskService;
    private final RuntimeService runtimeService;
    private final RepositoryService repositoryService;
    private final HistoryService historyService;
    private final ActivityUserTaskRepository activityUserTaskRepository;
    private final ActivityUserTaskMapper activityUserTaskMapper;
    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;

    @Override
    public TaskDetailsResponse getTaskFullContext(String taskId) {

        // =========================
        // 1. TASK
        // =========================
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();

        if (task == null) {
            throw new RuntimeException("Task introuvable : " + taskId);
        }

        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setId(task.getId());
        taskInfo.setName(task.getName());
        taskInfo.setAssignee(task.getAssignee());
        taskInfo.setCreateTime(task.getCreateTime());
        taskInfo.setDueDate(task.getDueDate());
        taskInfo.setProcessInstanceId(task.getProcessInstanceId());
        taskInfo.setProcessDefinitionId(task.getProcessDefinitionId());
        taskInfo.setTaskDefinitionKey(task.getTaskDefinitionKey());
        taskInfo.setPriority(task.getPriority());

        // =========================
        // 2. PROCESS DEFINITION
        // =========================
        ProcessDefinition processDefinition =
                repositoryService.getProcessDefinition(task.getProcessDefinitionId());

        ProcessDefinitionInfo processDefInfo = new ProcessDefinitionInfo();
        processDefInfo.setId(processDefinition.getId());
        processDefInfo.setKey(processDefinition.getKey());
        processDefInfo.setName(processDefinition.getName());
        processDefInfo.setVersion(processDefinition.getVersion());
        processDefInfo.setDeploymentId(processDefinition.getDeploymentId());

        // =========================
        // 3. VARIABLES DU PROCESS
        // =========================
        Map<String, Object> variables =
                runtimeService.getVariables(task.getProcessInstanceId());

        // =========================
        // 4. HISTORIQUE DES ÉTAPES
        // =========================
        List<HistoricActivityInstance> activities =
                historyService.createHistoricActivityInstanceQuery()
                        .processInstanceId(task.getProcessInstanceId())
                        .orderByHistoricActivityInstanceStartTime()
                        .asc()
                        .list();

        List<ActivityHistoryInfo> history = activities.stream().map(act -> {
            ActivityHistoryInfo info = new ActivityHistoryInfo();
            info.setActivityId(act.getActivityId());
            info.setActivityName(act.getActivityName());
            info.setActivityType(act.getActivityType());
            info.setStartTime(act.getStartTime());
            info.setEndTime(act.getEndTime());
            info.setAssignee(act.getAssignee());
            return info;
        }).collect(Collectors.toList());

        // =========================
        // 5. RÉSULTAT FINAL
        // =========================
        TaskDetailsResponse response = new TaskDetailsResponse();
        response.setTask(taskInfo);
        response.setProcessDefinition(processDefInfo);
        response.setProcessVariables(variables);
        response.setActivityHistory(history);

        ActivityUserTask activityUserTask = activityUserTaskRepository.findByTaskDefinitionKey(task.getTaskDefinitionKey());
        if (activityUserTask != null) {
            response.setActivityUserTask(activityUserTaskMapper.toDto(activityUserTask));
        }

        String businessKey = runtimeService
                .createProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .singleResult()
                .getBusinessKey();

        List<Document> documents = documentRepository.findByBusinessKey(businessKey);
        List<DocumentInfo> documentInfos = documentMapper.toDto(documents);
        response.setDocuments(documentInfos);

        return response;
    }


}
