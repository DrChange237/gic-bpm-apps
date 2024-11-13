package com.ccabank.memoservice.service.faces;

import com.ccabank.memoservice.entity.ApprovalStatus;
import org.camunda.bpm.engine.form.StartFormData;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.cmmn.execution.CaseExecutionState;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

import java.util.List;
import java.util.Map;

public interface CamundaService {

    ProcessInstance createProcessInstance(String processDefinitionKey, Map<String, Object> variables);

    void setProcessVariables(String processInstanceId, Map<String, Object> variables);

    // Démarrer un processus à partir d'une instance en attente
    void resumeProcessInstance(String instanceId, Map<String, Object> variables);

    Map<String, Object> getProcessVariables(String instanceId);

    List<ProcessInstance> getProcessInstancesForUser(String userId);

    List<HistoricProcessInstance> getProcessInstancesForUserWithStatus(String userId, CaseExecutionState status);

    List<HistoricTaskInstance> getExecutedTasksForProcessInstance(String processInstanceId);

    Task getTaskDetails(String taskId);

    List<Task> getTasksForProcessInstance(String processInstanceId);

    List<Task> getTasksAssignedToUser(String userId);

    List<Task> getTasksAssignedToUserWithStatus(String userId, ApprovalStatus status);

    StartFormData getStartForm(String processDefinitionKey);


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
