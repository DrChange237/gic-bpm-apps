package com.ccabank.memoservice.service.faces;

import com.ccabank.memoservice.entity.ApprovalStatus;
import org.camunda.bpm.engine.form.FormData;
import org.camunda.bpm.engine.form.StartFormData;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.cmmn.execution.CaseExecutionState;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CamundaService {

    //-------------------------------------------Process Instance---------------------------------------------------------
    ProcessDefinition getProcessDefinition(String processDefinitionId);

    ProcessInstance getProcessInstance(String processInstanceId);

    ProcessInstance createProcessInstance(String processDefinitionKey, String businessKey, Map<String, Object> variables);

    void setProcessVariables(String processInstanceId, Map<String, Object> variables);


    void setProcessVariable(String processInstanceId, String variableName, Object value);

    Map<String, Object> getProcessVariables(String instanceId);

    List<ProcessInstance> getProcessInstancesForUser(String userId);

    List<HistoricProcessInstance> getProcessInstancesForUserWithStatus(String userId, CaseExecutionState status);

    List<HistoricTaskInstance> getHistoricTasksForProcessInstance(String processInstanceId);

    List<HistoricActivityInstance> getHistoricActivityInstances(String processInstanceId);

    List<HistoricTaskInstance> getExecutedTasksForProcessInstance(String processInstanceId);

    Task getTaskDetails(String taskId);

    List<String> getAllAssigneInTask(String taskId);

    String getTaskAssigneeNature(String taskId);

    void cancelOthersToken(String processInstanceId, String executionId);

    Optional<HistoricTaskInstance> getLastHistoricTaskInstance(String processInstanceId, String taskDefinitionKey);


    void triggerProcessRestart(String messageName, String processInstanceId);

    Object getProcessVariable(String processInstanceId, String variableName);

    void createIncident(String processInstanceId, String incidentType, String incidentMessage);

    boolean isTaskAssignedToGroup(String taskId, String groupId);

    List<Task> getTasksForProcessInstance(String processInstanceId);

    Task getOneTaskForProcessInstanceAndKey(String processInstanceId, String definitionKey);

    boolean isUserInCandidateGroups(String taskId, String username);

    boolean isTaskCandidateGroup(String taskId);

    void completeTask(String taskId, Map<String, Object> variables);

    List<Task> getTasksAssignedToUser(String userId);

    void claimTask(String taskId, String userId);


    void addLocalVariableToTask(String taskId, String variableName, Object value);


    List<Task> getActiveTasksForUser(String userId);


    List<Task> getAllTasksForUser();

    List<HistoricTaskInstance> getConfirmTasksForUser(String userId, boolean decision);

    HistoricTaskInstance getHistoryTaskInstance(String taskId);

    void stopAllActiveProcessInstances();

    List<Task> getActiveTasksByAssignee(String username);

    List<Task> getTasksAssignedToUserWithStatus(String userId, ApprovalStatus status);

    Task getTaskByProcessInstanceIdAndTaskKey(String processInstanceId, String taskDefinitionKey);

    void assignTask(String processInstanceId, String taskId, String assignee);

    StartFormData getStartForm(String processDefinitionKey);


    FormData getFormData(String taskId);

    List<TaskFormData> getUserTasksWithForms(String processDefinitionKey);

    Map<String, Object>  retrieveCompletedProcessVariables(String processInstanceId);

    void createGroup(String groupId, String groupName, String groupType);

    void updateGroup(String groupId, String newName, String newType);

    // Ajouter un utilisateur au groupe
    void addUserToGroup(String userId, String groupId);

    // Supprimer un utilisateur du groupe
    void removeUserFromGroup(String userId, String groupId);

    // Remplacer la liste des membres du groupe
    void updateGroupMembers(String groupId, List<String> newMemberIds);

    List<Group> getAllGroup();

    Group getGroup(String groupId);

    List<User> getGroupDetailsWithMembers(String groupId);
}
