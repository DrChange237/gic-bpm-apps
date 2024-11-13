package com.ccabank.memoservice.service.impl;

import com.ccabank.memoservice.entity.ApprovalStatus;
import com.ccabank.memoservice.service.faces.CamundaService;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.form.StartFormData;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstanceQuery;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.cmmn.execution.CaseExecutionState;
import org.camunda.bpm.engine.impl.persistence.entity.GroupEntity;
import org.camunda.bpm.engine.impl.persistence.entity.UserEntity;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.rest.GroupRestService;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.camunda.bpm.engine.spring.annotations.UserId;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.camunda.bpm.engine.impl.cmmn.execution.CaseExecutionState.*;

@Service
public class CamundaServiceImpl implements CamundaService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private FormService formService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    private GroupRestService groupRestService;

    //-------------------------------------------Process Instance---------------------------------------------------------

    @Override
    public ProcessInstance createProcessInstance(String processDefinitionKey, Map<String, Object> variables) {
        // Créer une instance de processus sans la démarrer
        // Note : Camunda ne permet pas de créer une instance sans la démarrer,
        // mais vous pouvez stocker les variables pour un démarrage ultérieur.

        // Vous pouvez utiliser une variable de type "Waiting"
        variables.put("waiting", true); // Indiquer que le processus est en attente

        // Démarre le processus avec les variables
        return runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);
    }

    @Override
    public void setProcessVariables(String processInstanceId, Map<String, Object> variables) {
        runtimeService.setVariables(processInstanceId, variables);
    }

    @Override
    // Démarrer un processus à partir d'une instance en attente
    public void resumeProcessInstance(String instanceId, Map<String, Object> variables) {
        // Récupérer l'exécution associée à l'ID d'instance
        Execution execution = runtimeService.createExecutionQuery()
                .executionId(instanceId)
                .singleResult();

        if (execution == null) {
            throw new IllegalArgumentException("Execution with ID " + instanceId + " not found.");
        }

        // Envoyer un signal pour reprendre le processus
        runtimeService.signal(execution.getId(), variables);
    }

    @Override
    public Map<String, Object> getProcessVariables(String instanceId) {
        // Récupérer les variables de l'instance de processus
        Map<String, Object> variables = runtimeService.getVariables(instanceId);

        // Convertir VariableMap en Map<String, Object>
        Map<String, Object> result = new HashMap<>();
        for (String key : variables.keySet()) {
            result.put(key, variables.get(key));
        }

        return result;
    }

    @Override
    public List<ProcessInstance> getProcessInstancesForUser(String userId) {
        // Créer une requête pour récupérer les instances de processus en fonction de l'utilisateur
        ProcessInstanceQuery query = runtimeService.createProcessInstanceQuery()
                .variableValueEquals("assignee", userId); // Assurez-vous que la variable est correctement définie dans votre modèle de processus

        // Exécuter la requête et retourner la liste des instances
        return query.list();
    }

    @Override
    public List<HistoricProcessInstance> getProcessInstancesForUserWithStatus(String userId, CaseExecutionState status) {
        // Créer une requête pour récupérer les instances de processus en fonction de l'utilisateur et du statut
        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery()
                .variableValueEquals("assignee", userId); // Ajustez cette variable selon votre modèle

        // Filtrer par statut
        if (status.equals(ACTIVE)) {
            query.active(); // Récupérer les instances actives
        } else if (status.equals(SUSPENDED)) {
            query.suspended(); // Récupérer les instances suspendues
        } else if (status.equals(COMPLETED)) {
            query.finished(); // Récupérer les instances suspendues
        }
        // Exécuter la requête et retourner la liste des instances
        return query.list();
    }

    @Override
    public List<HistoricTaskInstance> getExecutedTasksForProcessInstance(String processInstanceId) {
        // Créer une requête pour récupérer les instances de tâches historiques associées à une instance de processus
        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .finished(); // Récupérer uniquement les tâches qui ont été exécutées

        // Exécuter la requête et retourner la liste des tâches
        return query.list();
    }

    //-------------------------------------------Task Function---------------------------------------------------------

    @Override
    public Task getTaskDetails(String taskId) {
        // Récupérer la tâche par son ID
        return taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
    }

    @Override
    public List<Task> getTasksForProcessInstance(String processInstanceId) {
        // Créer une requête pour récupérer toutes les tâches associées à l'instance de processus
        TaskQuery query = taskService.createTaskQuery()
                .processInstanceId(processInstanceId);

        // Exécuter la requête et retourner la liste des tâches
        return query.list();
    }

    @Override
    public List<Task> getTasksAssignedToUser(String userId) {
        // Créer une requête pour récupérer les tâches assignées à l'utilisateur
        TaskQuery query = taskService.createTaskQuery()
                .taskAssignee(userId); // Filtrer par utilisateur

        // Exécuter la requête et retourner la liste des tâches
        return query.list();
    }

    @Override
    public List<Task> getTasksAssignedToUserWithStatus(String userId, ApprovalStatus status) {
        // Créer une requête pour récupérer les tâches assignées à l'utilisateur
        TaskQuery query = taskService.createTaskQuery()
                .taskAssignee(userId); // Filtrer par utilisateur

        // Filtrer par statut
        switch (status) {
            case PENDING:
                query.active(); // Récupérer les tâches actives
                break;

            case REJECTED:
                query.suspended(); // Récupérer les tâches suspendues
                break;
            default:
                // Aucun filtrage supplémentaire
                break;
        }

        // Exécuter la requête et retourner la liste des tâches
        return query.list();
    }


    //-------------------------------------------Form Function---------------------------------------------------------

    @Override
    public StartFormData getStartForm(String processDefinitionKey) {
        // Récupérer la définition de processus
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey)
                .latestVersion()
                .singleResult();

        // Récupérer le formulaire de démarrage
        if (processDefinition != null) {
            return formService.getStartFormData(processDefinition.getId());
        } else {
            throw new IllegalArgumentException("Process definition not found for key: " + processDefinitionKey);
        }
    }

    //-------------------------------------------Group Function---------------------------------------------------------

    @Override
    public void createGroup(String groupId, String groupName, String groupType) {
        // Créer une nouvelle instance de GroupEntity
        GroupEntity group = new GroupEntity();
        group.setId(groupId);
        group.setName(groupName);
        group.setType(groupType);
        // Enregistrer le groupe via le service d'identité
        identityService.saveGroup(group);
    }

    @Override
    public void updateGroup(String groupId, String newName, String newType) {
        // Récupérer le groupe existant
        GroupEntity group = (GroupEntity) identityService.createGroupQuery()
                .groupId(groupId)
                .singleResult();

        if (group != null) {
            // Mettre à jour les propriétés du groupe
            group.setName(newName);
            group.setType(newType);

            // Enregistrer les modifications
            identityService.saveGroup(group);
        } else {
            throw new IllegalArgumentException("Group with ID " + groupId + " not found.");
        }
    }

    // Ajouter un utilisateur au groupe
    @Override
    public void addUserToGroup(String userId, String groupId) {
        User user = new UserEntity();
        String filterUserId = userId.replace(".","");       // Vérifier que l'utilisateur et le groupe existent
        if (identityService.createUserQuery().userId(filterUserId).count() == 0) {
            user = this.newUser(userId);
            //throw new IllegalArgumentException("User with ID " + userId + " not found.");
        }
        if (identityService.createGroupQuery().groupId(groupId).count() == 0) {
            throw new IllegalArgumentException("Group with ID " + groupId + " not found.");
        }

        // Ajouter l'utilisateur au groupe
        identityService.createMembership(filterUserId, groupId);
    }

    public User newUser(String userId) {
        User user = new UserEntity();
        user.setEmail(userId + "@cca-bank.com");
        String[] fullname = userId.split("\\.");
        user.setFirstName(fullname[0].toUpperCase());
        user.setLastName(fullname[1].toUpperCase());
        String newUserId = userId.replace(".", "");
        user.setId(newUserId);
        identityService.saveUser(user);
        return user;
    }

    // Supprimer un utilisateur du groupe
    @Override
    public void removeUserFromGroup(String userId, String groupId) {
        // Vérifier que l'utilisateur et le groupe existent
        if (identityService.createUserQuery().userId(userId).count() == 0) {
            throw new IllegalArgumentException("User with ID " + userId + " not found.");
        }
        if (identityService.createGroupQuery().groupId(groupId).count() == 0) {
            throw new IllegalArgumentException("Group with ID " + groupId + " not found.");
        }

        // Supprimer l'utilisateur du groupe
        identityService.deleteMembership(userId, groupId);
    }

    // Remplacer la liste des membres du groupe
    @Override
    public void updateGroupMembers(String groupId, List<String> newMemberIds) {
        // Récupérer le groupe
        var group = identityService.createGroupQuery().groupId(groupId).singleResult();
        if (group == null) {
            throw new IllegalArgumentException("Group with ID " + groupId + " not found.");
        }

        // Supprimer tous les membres existants
        List<User> existingMembers = identityService.createUserQuery().memberOfGroup(groupId).list();

        System.out.println("Membre exists: " + existingMembers.toString());

        for (User memberId : existingMembers) {
            System.out.println(memberId);
            removeUserFromGroup(memberId.getId(), groupId);
        }

        // Ajouter les nouveaux membres
        for (String memberId : newMemberIds) {
            addUserToGroup(memberId, groupId);
        }
    }

    @Override
    public List<Group> getAllGroup(){
        return identityService.createGroupQuery().list();
    }

    @Override
    public Group getGroup(String groupId){
        return identityService.createGroupQuery().groupId(groupId).singleResult();
    }

    @Override
    public List<User> getGroupDetailsWithMembers(String groupId) {
        // Récupérer le groupe
        Group group = identityService.createGroupQuery()
                .groupId(groupId)
                .singleResult();

        if (group == null) {
            throw new IllegalArgumentException("Group with ID " + groupId + " not found.");
        }

        // Récupérer les membres du groupe
        List<User> members =   identityService.createUserQuery().memberOfGroup(groupId).list();


        return members;
    }


}
