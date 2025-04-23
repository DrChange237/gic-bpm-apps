package com.ccabank.memoservice.service.impl;

import com.ccabank.memoservice.entity.ApprovalStatus;
import com.ccabank.memoservice.entity.camunda.UserCamunda;
import com.ccabank.memoservice.repository.GroupRepository;
import com.ccabank.memoservice.service.faces.CamundaService;
import com.ccabank.memoservice.service.faces.UserCamundaService;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.form.FormData;
import org.camunda.bpm.engine.form.StartFormData;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.history.*;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.cmmn.execution.CaseExecutionState;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.camunda.bpm.engine.runtime.*;
import org.camunda.bpm.engine.task.IdentityLink;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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

    @Autowired
    private UserCamundaService userCamundaService;

    @Autowired
    private GroupRepository groupRepository;


    @Autowired
    private ManagementService managementService;





    //-------------------------------------------Process Instance---------------------------------------------------------
    @Override
    public ProcessDefinition getProcessDefinition(String processDefinitionId) {
        return repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();
    }

    @Override
    public ProcessInstance getProcessInstance(String processInstanceId) {
        ProcessInstanceQuery query = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId);

        // Exécuter la requête et retourner la liste des instances
        return query.singleResult();
    }

    @Override
    public ProcessInstance createProcessInstance(String processDefinitionKey, String businessKey,  Map<String, Object> variables) {
        // Créer une instance de processus sans la démarrer
        // Note : Camunda ne permet pas de créer une instance sans la démarrer,
        // mais vous pouvez stocker les variables pour un démarrage ultérieur.
        // Démarre le processus avec les variables
        return runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
    }

    @Override
    public void setProcessVariables(String processInstanceId, Map<String, Object> variables) {
        runtimeService.setVariables(processInstanceId, variables);
    }

    @Override
    public void setProcessVariable(String processInstanceId, String variableName, Object value) {
        runtimeService.setVariable(processInstanceId, variableName, value);
    }


    @Override
    public Map<String, Object> getProcessVariables(String processInstanceId) {
        // Récupérer les variables de l'instance de processus
        List<VariableInstance> variableInstances = runtimeService
                .createVariableInstanceQuery().processInstanceIdIn(processInstanceId)
                .list();

        System.out.println(variableInstances);

        // Convertir en Map pour un accès facile
        return variableInstances.stream()
                .filter(variable -> variable.getName() != null)  // Filtrer les noms null
                .filter(variable -> variable.getValue() != null)  // Filtrer les noms null
                .collect(Collectors.toMap(VariableInstance::getName, VariableInstance::getValue));
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
    public List<HistoricTaskInstance> getHistoricTasksForProcessInstance(String processInstanceId) {
        // Créer une requête pour récupérer les instances de tâches historiques
        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricTaskInstanceEndTime().desc(); // Optionnel: trier par date de fin

        // Exécuter la requête et retourner la liste des tâches historiques
        return query.list();
    }

    @Override
    public List<HistoricActivityInstance> getHistoricActivityInstances(String processInstanceId) {
        return historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .list();
    }

    @Override
    public List<IdentityLink> getTaskCandidates(String taskId) {
        // Récupérer les liens d'identité pour la tâche donnée
        List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(taskId);
        return identityLinks;
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
    public List<String> getAllAssigneInTask(String taskId) {
        List<String> assignes = new ArrayList<>();

        // Récupérer la tâche par son ID
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();

        // Vérifier si la tâche existe
        if (task == null) {
            System.out.println("TASK IS NULL");
            return assignes;
            //throw new IllegalArgumentException("Task not found with ID: " + taskId);
        }

        // Vérifier si l'assigné est un groupe ou un utilisateur
        String assignee = task.getAssignee();
        if(assignee != null) {
            assignes.add(assignee);
        }

        // Récupérer les groupes candidats
        List<String> candidateUsers = taskService.getIdentityLinksForTask(taskId).stream()
                .filter(link -> link.getUserId() != null)
                .map(link -> link.getUserId()).collect(Collectors.toList());

        if (!candidateUsers.isEmpty()) {
            // Si l'assigné est null, mais qu'il y a des groupes candidats, c'est un groupe
            assignes.addAll(candidateUsers);
        }

        // Récupérer les groupes candidats
        List<String> candidateGroups = taskService.getIdentityLinksForTask(taskId).stream()
                .filter(link -> link.getGroupId() != null)
                .map(link -> link.getGroupId()).collect(Collectors.toList());

        if (!candidateGroups.isEmpty()) {
            // Si l'assigné est null, mais qu'il y a des groupes candidats, c'est un groupe
            for (String groupId : candidateGroups) {
                List<User> users = identityService.createUserQuery().memberOfGroup(groupId).list();
                List<String> userNames = users.stream().map(User::getId).collect(Collectors.toList());
                assignes.addAll(userNames);
            }
        }

        return assignes;
    }

    @Override
    public String getTaskAssigneeNature(String taskId) {
        // Récupérer la tâche par son ID
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();

        // Vérifier si la tâche existe
        if (task == null) {
            return "GROUP";
            //throw new IllegalArgumentException("Task not found with ID: " + taskId);
        }

        // Vérifier si l'assigné est un groupe ou un utilisateur
        String assignee = task.getAssignee();

        // Récupérer les groupes candidats
        List<String> candidateGroups = taskService.getIdentityLinksForTask(taskId).stream()
                .filter(link -> link.getGroupId() != null)
                .map(link -> link.getGroupId()).collect(Collectors.toList());

        if (!candidateGroups.isEmpty()) {
            // Si l'assigné est null, mais qu'il y a des groupes candidats, c'est un groupe
            return "GROUP";
        }

        return "USER";

    }

    public String getTaskIdsByExecutionId(String executionId) {
        // Créer une requête pour obtenir les tâches associées à l'ID d'exécution
        TaskQuery taskQuery = taskService.createTaskQuery().executionId(executionId).active();
        // Obtenir la liste des tâches
        Task task = taskQuery.singleResult();
        System.out.println("la tache associé a cette executio ID est :" + task.getName());
        // Extraire et retourner les IDs des tâches
        return task.getId();
    }

    public void suspendTask(String taskId) {
        // Vérifier si la tâche existe
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();

        if (task != null) {
            // Suspendre la tâche
            taskService.setAssignee(taskId, null);
            taskService.claim(task.getId(), "notneed");
        } else {
            throw new IllegalArgumentException("Task not found for ID: " + taskId);
        }
    }

    @Override
    public void cancelOthersToken(String processInstanceId, String executionId) {
        List<Execution> executions = this.getActiveTokens(processInstanceId);
        for (Execution execution : executions) {
            System.out.println("Executing: " + execution.getId());
            if (!execution.getId().equals(executionId)) {
                String taskId = this.getTaskIdsByExecutionId(execution.getId());
                this.suspendTask(taskId);
            }
        }
    }


    public List<Execution> getActiveTokens(String processInstanceId) {
        // Récupérer l'instance de processus
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (processInstance != null) {
            // Récupérer les exécutions (jetons) actifs pour cette instance
            List<Execution> executions = runtimeService.createExecutionQuery()
                    .processInstanceId(processInstanceId)
                    .active() // Filtrer pour obtenir uniquement les jetons actifs
                    .list();
            return executions;
        } else {
            throw new IllegalArgumentException("Process instance not found for ID: " + processInstanceId);
        }
    }

    @Override
    public Optional<HistoricTaskInstance> getLastHistoricTaskInstance(String processInstanceId, String taskDefinitionKey) {
        // Créer une requête pour récupérer les instances historiques de tâches
        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .taskDefinitionKey(taskDefinitionKey).orderByHistoricTaskInstanceEndTime().desc();

        // Récupérer le premier résultat (le dernier HistoricTaskInstance)
        if (!query.list().isEmpty()) {
            return Optional.of(query.list().get(0));
        } else {
            return Optional.empty(); // Aucune instance trouvée
        }
    }

    @Override
    public void triggerProcessRestart(String messageName, String processInstanceId) {
        // Envoyer un message pour relancer le processus
        runtimeService
                .createMessageCorrelation(messageName)
                .processInstanceId(processInstanceId) // spécifier l'ID de l'instance de processus
                .correlateWithResult(); // Cela relancera l'instance si le message est attendu
    }

    @Override
    public Object getProcessVariable(String processInstanceId, String variableName) {
        // Vérifiez que l'instance de processus existe
        if (processInstanceId == null || variableName == null) {
            throw new IllegalArgumentException("Process instance ID and variable name must not be null");
        }

        // Récupérer la valeur de la variable
        Object variableValue = runtimeService.getVariable(processInstanceId, variableName);

        // Vérifiez si la variable existe
        if (variableValue == null) {
            return null;
        }

        return variableValue;
    }

    @Override
    public void createIncident(String processInstanceId, String incidentType, String incidentMessage) {
        // Créer un incident
        Incident incident = runtimeService.
                createIncident(incidentType, processInstanceId, incidentMessage);
        System.out.println("Incident created: " + incident.getId());
    }

    @Override
    public boolean isTaskAssignedToGroup(String taskId, String groupId) {
        TaskQuery query = taskService.createTaskQuery()
                .taskId(taskId)
                .taskCandidateGroup(groupId);

        List<Task> tasks = query.list();
        return !tasks.isEmpty(); // Retourne vrai si la tâche est assignée au groupe
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
    public Task getOneTaskForProcessInstanceAndKey(String processInstanceId, String definitionKey) {
        // Créer une requête pour récupérer toutes les tâches associées à l'instance de processus
        TaskQuery query = taskService.createTaskQuery()
                .processInstanceId(processInstanceId).taskDefinitionKey(definitionKey);

        // Exécuter la requête et retourner la liste des tâches
        return query.singleResult();
    }

    @Override
    public boolean isUserInCandidateGroups(String taskId, String username) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task != null) {
            List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(taskId);
            for (IdentityLink link : identityLinks) {
                if (link.getGroupId() != null && identityService.createGroupQuery().groupId(link.getGroupId()).count() > 0) {
                    if (identityService.createUserQuery().userId(username).memberOfGroup(link.getGroupId()).count() > 0) {
                        return true; // L'utilisateur fait partie du groupe candidat
                    }
                }
            }
        }
        return false; // L'utilisateur n'est pas dans les groupes candidats
    }

    @Override
    public boolean isTaskCandidateGroup(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task != null) {
            List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(taskId);
            // Vérifie si au moins un groupe candidat est présent
            return identityLinks.stream().anyMatch(link -> link.getGroupId() != null);
        }
        return false; // La tâche n'existe pas ou n'a pas de groupes candidats
    }

    @Override
    public void completeTask(String taskId, Map<String, Object> variables) {
        // Créer une requête pour récupérer toutes les tâches associées à l'instance de processus
        taskService.complete(taskId, variables);
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
    public void claimTask(String taskId, String userId) {
        // Revendiquer la tâche pour l'utilisateur spécifié
        taskService.claim(taskId, userId);
    }

    @Override
    public void addLocalVariableToTask(String taskId, String variableName, Object value) {
        // Récupérer la tâche par son ID
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        if (task != null) {
            // Ajouter la variable locale à la tâche
            taskService.removeVariable(taskId, variableName);
            taskService.setVariable(taskId, variableName, value);
        } else {
            throw new IllegalArgumentException("Task not found with ID: " + taskId);
        }
    }


    @Override
    public List<Task> getActiveTasksForUser(String userId) {
        // Récupérer les groupes de l'utilisateur
        List<Group> groups = identityService.createGroupQuery().groupMember(userId).list();

        List<String> groupIds = groups.stream().map(group -> group.getId()).collect(Collectors.toList());

        if (groupIds.isEmpty()){
            TaskQuery taskQuery = taskService.createTaskQuery()
                    .active() // Récupérer uniquement les tâches actives
                    .or()
                    .taskAssignee(userId) // Tâches assignées directement à l'utilisateur
                    .endOr();

            // Exécuter la requête et retourner la liste des tâches
            return taskQuery.list();
        }


        // Créer une requête de tâches pour les tâches assignées aux groupes
        TaskQuery taskQuery = taskService.createTaskQuery()
                .active() // Récupérer uniquement les tâches actives
                .or()
                .taskCandidateGroupIn(groupIds) // Tâches candidates pour les groupes
                .taskAssignee(userId) // Tâches assignées directement à l'utilisateur
                .endOr();

        // Exécuter la requête et retourner la liste des tâches
        return taskQuery.list();
    }

    @Override
    public List<Task> getAllTasksForUser() {
        // Récupérer les groupes de l'utilisateur
        List<Group> groups = identityService.createGroupQuery().list();

        List<String> groupIds = groups.stream().map(group -> group.getId()).collect(Collectors.toList());

        // Créer une requête de tâches pour les tâches assignées aux groupes
        TaskQuery taskQuery = taskService.createTaskQuery()
                .active() // Récupérer uniquement les tâches actives
                .or()
                .endOr();

        // Exécuter la requête et retourner la liste des tâches
        return taskQuery.list();
    }


    @Override
    public List<HistoricTaskInstance> getConfirmTasksForUser(String userId, boolean decision) {
        // Récupérer les groupes de l'utilisateur
        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery()
                .taskVariableValueEquals("signature", decision)
                .taskAssignee(userId) // Tâches assignées directement à l'utilisateur
                .finished(); // Filtrer uniquement les tâches complétées

        // Exécuter la requête et retourner la liste des tâches
        return query.list();
    }

    @Override
    public HistoricTaskInstance getHistoryTaskInstance(String taskId) {
        // Vérifiez d'abord si la tâche existe
        return historyService.createHistoricTaskInstanceQuery()
                .taskId(taskId)
                .singleResult();
    }

    @Override
    public void stopAllActiveProcessInstances() {
        // Récupérer toutes les instances de processus actives
        List<ProcessInstance> activeInstances = runtimeService.createProcessInstanceQuery()
                .active() // Filtrer pour les instances actives
                .list();

        // Parcourir et supprimer chaque instance active
        for (ProcessInstance processInstance : activeInstances) {
            runtimeService.deleteProcessInstance(processInstance.getId(), "Stopped by admin"); // Motif d'arrêt
        }

        System.out.println(activeInstances.size() + " instances de processus arrêtées.");
    }

    @Override
    public void deleteProcessInstance(String processInstanceId) {
        runtimeService.deleteProcessInstance(processInstanceId, "Stopped by admin"); // Motif d'arrêt
        System.out.println(" instances de processus arrêtées.");
    }


    @Override
    public List<Task> getActiveTasksByAssignee(String username) {
        return taskService.createTaskQuery()
                .taskAssignee(username) // Filtrer par utilisateur assigné
                .active() // Récupérer uniquement les tâches actives
                .list(); // Exécuter la requête et retourner la liste
    }

    @Override
    public List<Task> getActiveTasksByInstance(String processInstanceId) {
        return taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .active() // Récupérer uniquement les tâches actives
                .list(); // Exécuter la requête et retourner la liste
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

    @Override
    public Task getTaskByProcessInstanceIdAndTaskKey(String processInstanceId, String taskDefinitionKey) {
        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .taskDefinitionKey(taskDefinitionKey)
                .list();

        if (!tasks.isEmpty()) {
            return tasks.get(0); // Retourne la première tâche trouvée
        }
        return null; // Ou lance une exception selon vos besoins
    }

    @Override
    public void assignTask(String processInstanceId, String taskId, String assignee) {

        // Vérifier que l'utilisateur et le groupe existent
        /*UserCamunda user = userCamundaService.getUser(assignee);
        if (identityService.createUserQuery().userId(user.getId()).count() == 0) {
            userCamundaService.newUser(assignee);
            //throw new IllegalArgumentException("User with ID " + userId + " not found.");
        }

        if (taskService.createTaskQuery().taskId(taskId).singleResult() == null) {
            throw new IllegalArgumentException("Task not found with ID: " + taskId);
        }*/

        // Réassigner la tâche à un nouvel utilisateur
        taskService.setAssignee(taskId, assignee);

        /*taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .taskDefinitionKey(taskId) // Utilisez la clé de la tâche
                .list()
                .forEach(task -> {
                    if(task.getId() != null){
                        System.out.println(task.getName());
                        taskService.setAssignee(task.getId(), user.getId());
                    }
                });*/
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




    @Override
    public FormData getFormData(String taskId) {
        return formService.getTaskFormData(taskId);
    }

    @Override
    public List<TaskFormData> getUserTasksWithForms(String processDefinitionKey) {
        List<TaskFormData> tasksWithForms = new ArrayList<>();

        ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey);

        List<ProcessDefinition> definitions = query.list();

        for (ProcessDefinition definition : definitions) {
            BpmnModelInstance modelInstance = repositoryService.getBpmnModelInstance(definition.getId());
            Collection<UserTask> userTasks = modelInstance.getModelElementsByType(UserTask.class);

            for (UserTask userTask : userTasks) {
                TaskFormData taskFormData = formService.getTaskFormData(userTask.getId());
                if (taskFormData != null) {
                    tasksWithForms.add(taskFormData);
                }
            }
        }
        return tasksWithForms;
    }

    @Override
    public Map<String, Object>  retrieveCompletedProcessVariables(String processInstanceId) {
        HistoricVariableInstanceQuery query = historyService
                .createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId);

        List<HistoricVariableInstance> variables = query.list();

        Map<String, Object> variablesMap = new HashMap<>();

        for (HistoricVariableInstance variable : variables) {
            System.out.println("Variable Name: " + variable.getName() + ", Value: " + variable.getValue());
            variablesMap.put(variable.getName(), variable.getValue());
        }

        return variablesMap;
    }

    //-------------------------------------------Group Function---------------------------------------------------------

    @Override
    public void createGroup(String groupId, String groupName, String groupType) {
        // Créer une nouvelle instance de GroupEntity
        com.ccabank.memoservice.entity.camunda.Group group = new com.ccabank.memoservice.entity.camunda.Group();
        group.setId(groupId);
        group.setName(groupName);
        group.setType("PROCESS-UNITY");
        // Enregistrer le groupe via le service d'identité
        groupRepository.save(group);
    }

    @Override
    public void updateGroup(String groupId, String newName, String newType) {

        Optional<com.ccabank.memoservice.entity.camunda.Group> groupOptional = groupRepository.findById(groupId);
        if (groupOptional.isPresent()) {
            com.ccabank.memoservice.entity.camunda.Group group = groupOptional.get();
            group.setId(groupId);
            group.setName(newName);
            group.setType("PROCESS-UNITY");
            // Enregistrer le groupe via le service d'identité
            groupRepository.save(group);
        }
    }

    // Ajouter un utilisateur au groupe
    @Override
    @Transactional
    public void addUserToGroup(String userId, String groupId) {
        userCamundaService.newMembership(userId,groupId);
    }


    // Supprimer un utilisateur du groupe
    @Override
    public void removeUserFromGroup(String userId, String groupId) {

        UserCamunda user = userCamundaService.getUser(userId);

        // Vérifier que l'utilisateur et le groupe existent
        if (identityService.createUserQuery().userId(user.getId()).count() == 0) {
            throw new IllegalArgumentException("User with ID " + userId + " not found.");
        }
        if (identityService.createGroupQuery().groupId(groupId).count() == 0) {
            throw new IllegalArgumentException("Group with ID " + groupId + " not found.");
        }

        // Supprimer l'utilisateur du groupe
        identityService.deleteMembership(user.getId(), groupId);
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
            UserCamunda user = userCamundaService.getUserById(memberId.getId());
            removeUserFromGroup(user.getId(), groupId);
        }

        // Ajouter les nouveaux membres
        for (String memberId : newMemberIds) {
            addUserToGroup(memberId, groupId);
        }
    }

    @Override
    public List<Group> getAllGroup(){
        return identityService.createGroupQuery().groupType("PROCESS-UNITY").list();
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
