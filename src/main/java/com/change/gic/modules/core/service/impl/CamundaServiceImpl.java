package com.change.gic.modules.core.service.impl;

import com.change.gic.modules.core.dto.camunda.ProcessStartResponse;
import com.change.gic.modules.core.dto.camunda.form.*;
import com.change.gic.modules.core.service.faces.CamundaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.authorization.Permissions;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CamundaServiceImpl implements CamundaService {

    private final IdentityService identityService;
    private final RepositoryService repositoryService;
    private final ObjectMapper objectMapper;
    private final TaskService taskService;
    private final RuntimeService runtimeService;
    private final FormService formService;
    private final AuthorizationService authorizationService;


    @Override
    public boolean canUserStartProcess(String processDefinitionKey, String username) {

        // 1️⃣ Récupérer la process definition
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey)
                .latestVersion()
                .singleResult();

        if (processDefinition == null) {
            return false;
        }

        String processDefinitionId = processDefinition.getId();

        // 2️⃣ Vérifier autorisation directe utilisateur
        boolean hasUserAuth = authorizationService
                .createAuthorizationQuery()
                .userIdIn(username)
                .resourceType(Resources.PROCESS_DEFINITION)
                .resourceId(processDefinitionId)
                .hasPermission(Permissions.CREATE_INSTANCE)
                .count() > 0;

        if (hasUserAuth) {
            return true;
        }

        // 3️⃣ Récupérer groupes de l'utilisateur
        List<String> groupIds = identityService
                .createGroupQuery()
                .groupMember(username)
                .list()
                .stream()
                .map(Group::getId)
                .collect(Collectors.toList());

        if (groupIds.isEmpty()) {
            return false;
        }

        // 4️⃣ Vérifier autorisation via groupe
        return authorizationService
                .createAuthorizationQuery()
                .groupIdIn(groupIds.toArray(new String[0]))
                .resourceType(Resources.PROCESS_DEFINITION)
                .resourceId(processDefinitionId)
                .hasPermission(Permissions.CREATE_INSTANCE)
                .count() > 0;
    }


    @Override
    public void claim(String taskId, String userId) {
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();

        if (task == null) {
            throw new RuntimeException("Task not found");
        }

        if (task.getAssignee() != null) {
            throw new RuntimeException("Task already claimed");
        }

        taskService.claim(taskId, userId);
    }

    @Override
    public void unclaim(String taskId) {
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();

        if (task == null) {
            throw new RuntimeException("Task not found");
        }

        taskService.setAssignee(taskId, null);
    }

    /**
     * Démarre un processus avec données du formulaire et utilisateur initiateur
     */
    @Override
    public ProcessStartResponse startProcessWithInitiator(String processDefinitionKey,
                                                          Map<String, Object> formData,
                                                          String businessKey,
                                                          String initiatorUserId) {
        try {
            // Ajouter l'initiateur aux variables
            Map<String, Object> variables = new HashMap<>(formData);
            variables.put("initiator", initiatorUserId);
            variables.put("startDate", new java.util.Date());

            log.info("Démarrage du processus: {} par l'utilisateur: {}",
                    processDefinitionKey, initiatorUserId);

            ProcessInstance processInstance = runtimeService
                    .startProcessInstanceByKey(
                            processDefinitionKey,
                            businessKey,
                            variables
                    );

            return ProcessStartResponse.builder()
                    .success(true)
                    .processInstanceId(processInstance.getProcessInstanceId())
                    .businessKey(processInstance.getBusinessKey())
                    .message("Processus démarré avec succès")
                    .build();

        } catch (Exception e) {
            log.error("Erreur lors du démarrage du processus: {}", e.getMessage(), e);
            return ProcessStartResponse.builder()
                    .success(false)
                    .message("Erreur: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Récupère toutes les tâches (assignées + candidates) pour un utilisateur
     */
    @Override
    public List<Task> getAllTasksForUser(String userId) {
        // Récupérer les tâches assignées
        List<Task> assignedTasks = taskService.createTaskQuery()
                .taskAssignee(userId)
                .list();

        // Récupérer les tâches candidates
        List<Task> candidateTasks = taskService.createTaskQuery()
                .taskCandidateUser(userId)
                .list();

        // Fusionner les deux listes et éliminer les doublons
        List<Task> allTasks = new java.util.ArrayList<>(assignedTasks);
        candidateTasks.stream()
                .filter(task -> assignedTasks.stream()
                        .noneMatch(t -> t.getId().equals(task.getId())))
                .forEach(allTasks::add);

        // Trier par date de création (plus récent en premier)
        allTasks.sort((t1, t2) -> t2.getCreateTime().compareTo(t1.getCreateTime()));

        return  allTasks;
    }

    @Override
    public List<Task> getAllTasksForUser() {
        // Récupérer les groupes de l'utilisateur
        // Créer une requête de tâches pour les tâches assignées aux groupes
        TaskQuery taskQuery = taskService.createTaskQuery()
                .active() // Récupérer uniquement les tâches actives
                .or()
                .endOr();

        // Exécuter la requête et retourner la liste des tâches
        return taskQuery.list();
    }

    @Override
    public List<Task> getActiveTasksByBusinessKey(String businessKey) {
        return taskService.createTaskQuery()
                .active()
                .processInstanceBusinessKey(businessKey)
                .list();
    }



    /**
     * Retourne la liste des processus que l'utilisateur peut démarrer
     */
    @Override
    public List<ProcessDefinition> getProcessesForUser(String userId) {

        // On récupère les groupes de l'utilisateur
        List<String> groupIds = identityService.createGroupQuery()
                .groupMember(userId)
                .list()
                .stream()
                .map(g -> g.getId()).collect(Collectors.toList());

        // Requête pour récupérer les process definitions que l'utilisateur peut démarrer
        ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery()
                .latestVersion()
                .active();

        // Filtrer par user / groups autorisés
        if (!groupIds.isEmpty()) {
            query = query.startableByUser(userId);
        }

        return query.list();
    }

    /**
     * Récupère le formulaire Camunda avec métadonnées par clé de processus
     *
     * @param processDefinitionKey La clé de définition du processus
     * @return Le formulaire avec métadonnées
     */
    @Override
    public CamundaFormResponseDto getStartFormByProcessKey(String processDefinitionKey) {
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey)
                .latestVersion()
                .singleResult();

        if (processDefinition == null) {
            return buildErrorResponse("Process definition not found: " + processDefinitionKey);
        }

        return getStartFormByProcessDefinitionId(processDefinition.getId());
    }

    /**
     * Trouve le nom exact de la ressource du formulaire
     */
    private String findFormResource(List<String> resourceNames, String formResourceName) {
        // Recherche exacte
        if (resourceNames.contains(formResourceName)) {
            return formResourceName;
        }

        // Recherche sans extension
        String nameWithoutExtension = formResourceName.replace(".form", "");
        for (String resource : resourceNames) {
            if (resource.equals(nameWithoutExtension) ||
                    resource.endsWith("/" + formResourceName) ||
                    resource.endsWith("\\" + formResourceName)) {
                return resource;
            }
        }
        return null;
    }

    /**
     * Essaie de récupérer le formulaire depuis un déploiement spécifique
     */
    private String tryGetFormFromDeployment(String deploymentId, String formResourceName) {
        try {
            List<String> resourceNames = repositoryService.getDeploymentResourceNames(deploymentId);
            String actualResourceName = findFormResource(resourceNames, formResourceName);

            if (actualResourceName == null) {
                return null;
            }

            InputStream formStream = repositoryService.getResourceAsStream(deploymentId, actualResourceName);

            if (formStream == null) {
                return null;
            }

            return new String(formStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Cherche le formulaire dans tous les déploiements (du plus récent au plus ancien)
     */
    private String searchFormInAllDeployments(String formResourceName) {
        try {
            // Récupérer tous les déploiements par ordre décroissant de date
            List<org.camunda.bpm.engine.repository.Deployment> deployments =
                    repositoryService.createDeploymentQuery()
                            .orderByDeploymentTime()
                            .desc()
                            .list();

            for (org.camunda.bpm.engine.repository.Deployment deployment : deployments) {
                String formContent = tryGetFormFromDeployment(deployment.getId(), formResourceName);
                if (formContent != null) {
                    return formContent;
                }
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Récupère le formulaire Camunda avec métadonnées par ID de définition
     *
     * @param processDefinitionId L'ID de définition du processus
     * @return Le formulaire avec métadonnées
     */
    @Override
    public CamundaFormResponseDto getStartFormByProcessDefinitionId(String processDefinitionId) {
        try {
            ProcessDefinition processDefinition = repositoryService
                    .createProcessDefinitionQuery()
                    .processDefinitionId(processDefinitionId)
                    .singleResult();

            if (processDefinition == null) {
                return buildErrorResponse("Process definition not found: " + processDefinitionId);
            }

            // Récupérer la clé du formulaire
            String formKey = getStartEventFormKey(processDefinitionId);
            log.info("Form key: " + formKey);

            if (formKey == null || formKey.isEmpty()) {
                // Construire la réponse
                return CamundaFormResponseDto.builder()
                        .success(true)
                        .message("Form retrieved successfully")
                        .build();
                //return buildErrorResponse("No form key found for process: " + processDefinitionId);
            }

            // Préparer le nom de la ressource
            String formResourceName = prepareFormResourceName(formKey);
            log.info("Form resource name: " + formResourceName);

            // Récupérer le contenu du formulaire
            //String formContent = getFormContent(processDefinition.getDeploymentId(), formResourceName);
            String formContent = searchFormInAllDeployments(formResourceName);
            log.info("Form content: " + formContent);

            // Parser le JSON en Dto
            CamundaFormDto formDto = objectMapper.readValue(formContent, CamundaFormDto.class);

            // Construire les métadonnées
            FormMetadataDto metadata = FormMetadataDto.builder()
                    .processDefinitionId(processDefinition.getId())
                    .processDefinitionKey(processDefinition.getKey())
                    .processDefinitionVersion(processDefinition.getVersion())
                    .formKey(formKey)
                    .formResourceName(formResourceName)
                    .deploymentId(processDefinition.getDeploymentId())
                    .build();

            // Ajouter les métadonnées au Dto du formulaire
            formDto.setMetadata(metadata);

            // Construire la réponse
            return CamundaFormResponseDto.builder()
                    .form(formDto)
                    .metadata(metadata)
                    .success(true)
                    .message("Form retrieved successfully")
                    .build();

        } catch (Exception e) {
            return buildErrorResponse("Error retrieving form: " + e.getMessage());
        }
    }

    /**
     * Récupère le formulaire avec gestion de version
     */
    @Override
    public CamundaFormResponseDto getStartFormByProcessKeyAndVersion(
            String processDefinitionKey, Integer version) {

        ProcessDefinition processDefinition;

        if (version != null) {
            processDefinition = repositoryService
                    .createProcessDefinitionQuery()
                    .processDefinitionKey(processDefinitionKey)
                    .processDefinitionVersion(version)
                    .singleResult();
        } else {
            processDefinition = repositoryService
                    .createProcessDefinitionQuery()
                    .processDefinitionKey(processDefinitionKey)
                    .latestVersion()
                    .singleResult();
        }

        if (processDefinition == null) {
            String errorMsg = "Process definition not found: " + processDefinitionKey +
                    (version != null ? " v" + version : "");
            return buildErrorResponse(errorMsg);
        }

        return getStartFormByProcessDefinitionId(processDefinition.getId());
    }

    /**
     * Retourne uniquement le JSON brut du formulaire
     */
    @Override
    public String getStartFormJsonByProcessKey(String processDefinitionKey) {
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey)
                .latestVersion()
                .singleResult();

        if (processDefinition == null) {
            throw new RuntimeException("Process definition not found: " + processDefinitionKey);
        }

        String formKey = getStartEventFormKey(processDefinition.getId());
        String formResourceName = prepareFormResourceName(formKey);

        return getFormContent(processDefinition.getDeploymentId(), formResourceName);
    }

    /**
     * Récupère la clé du formulaire depuis le StartEvent
     */
    @Override
    public String getStartEventFormKey(String processDefinitionId) {
        BpmnModelInstance modelInstance = repositoryService.getBpmnModelInstance(processDefinitionId);

        Collection<StartEvent> startEvents = modelInstance.getModelElementsByType(StartEvent.class);

        if (startEvents.isEmpty()) {
            throw new RuntimeException("No start event found in process: " + processDefinitionId);
        }

        StartEvent startEvent = startEvents.iterator().next();

        String formKey = startEvent.getCamundaFormKey();

        if (formKey == null) {
            formKey = startEvent.getCamundaFormRef();
        }

        return formKey;
    }

    /**
     * Récupère la clé du formulaire depuis le StartEvent
     */
    public String getTaskFormKey(String processDefinitionId, String taskDefinitionKey) {
        BpmnModelInstance modelInstance = repositoryService.getBpmnModelInstance(processDefinitionId);
        Collection<UserTask> startEvents = modelInstance.getModelElementsByType(UserTask.class);
        if (startEvents.isEmpty()) {
            throw new RuntimeException("No start event found in process: " + processDefinitionId);
        }

        String formKey = null;

        for (UserTask userTask : startEvents) {
            log.info(userTask.getId());
            log.info(userTask.getName());
            log.info(userTask.getCamundaFormKey());
            if(userTask.getId().equals(taskDefinitionKey)) {
                formKey = userTask.getCamundaFormKey();
                if (formKey == null) {
                    formKey = userTask.getCamundaFormRef();
                }

            }
        }
        return formKey;
    }

    /**
     * Prépare le nom de la ressource du formulaire
     */
    @Override
    public String prepareFormResourceName(String formKey) {
        String formResourceName = formKey.replace("camunda-forms:", "");

        if (!formResourceName.endsWith(".form")) {
            formResourceName += ".form";
        }

        return formResourceName;
    }

    /**
     * Récupère le contenu du fichier .form
     */
    @Override
    public String getFormContent(String deploymentId, String formResourceName) {
        try (InputStream formStream = repositoryService.getResourceAsStream(
                deploymentId, formResourceName)) {

            if (formStream == null) {
                throw new RuntimeException("Form resource not found: " + formResourceName);
            }

            return new String(formStream.readAllBytes(), StandardCharsets.UTF_8);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Error reading form content: " + formResourceName, e);
        }
    }

    /**
     * Construit une réponse d'erreur
     */
    private CamundaFormResponseDto buildErrorResponse(String errorMessage) {
        return CamundaFormResponseDto.builder()
                .success(false)
                .message(errorMessage)
                .build();
    }

    /**
     * Récupère le formulaire Camunda Forms (.form) associé à une tâche
     *
     * @param taskId L'identifiant de la tâche
     * @return Le contenu du formulaire en format JSON
     * @throws IllegalArgumentException si la tâche n'existe pas
     */
    @Override
    public CamundaFormResponseDto getTaskForm(String taskId) throws JsonProcessingException {
        // Vérifier que la tâche existe
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();

        if (task == null) {
            throw new IllegalArgumentException("Aucune tâche trouvée avec l'ID: " + taskId);
        }

        // Récupérer la clé du formulaire deployé


        String formKey =  getTaskFormKey(task.getProcessDefinitionId(), task.getTaskDefinitionKey());

        log.info("Form key: " + formKey);

        if (formKey == null || formKey.isEmpty()) {
            // Construire la réponse
            return CamundaFormResponseDto.builder()
                    .success(true)
                    .message("Form retrieved successfully")
                    .build();
            //return buildErrorResponse("No form key found for process: " + processDefinitionId);
        }

        // Préparer le nom de la ressource
        String formResourceName = prepareFormResourceName(formKey);
        log.info("Form resource name: " + formResourceName);

        // Récupérer le contenu du formulaire
        //String formContent = getFormContent(processDefinition.getDeploymentId(), formResourceName);
        String formContent = searchFormInAllDeployments(formResourceName);
        log.info("Form content: " + formContent);

        // Parser le JSON en Dto
        CamundaFormDto formDto = objectMapper.readValue(formContent, CamundaFormDto.class);

        // Construire les métadonnées
        FormMetadataDto metadata = FormMetadataDto.builder()
                .processDefinitionId(task.getProcessDefinitionId())
                .processDefinitionKey(task.getTaskDefinitionKey())
                .processDefinitionVersion(1)
                .formKey(formKey)
                .formResourceName(formResourceName)
                .deploymentId(task.getExecutionId())
                .build();

        // Ajouter les métadonnées au Dto du formulaire
        formDto.setMetadata(metadata);

        Map<String, Object> variables =  this.getTaskFormVariables(taskId);
        List<FormComponentDto> components = new ArrayList<>();

        for(FormComponentDto component :  formDto.getComponents() ){

            FormComponentDto c = component;


               log.info(c.getKey());
               log.info(c.getType());

               switch (c.getType()){
                   case "text":
                       if(variables.get(c.getKey() + "_text") != null){
                           c.setText((String) variables.get(c.getKey() + "_text"));
                       }
                       break;
                   case "select":
                       if(variables.get(c.getKey() + "_values") != null){
                           c.setValues((List<SelectOptionDto>) variables.get(c.getKey() + "_values"));
                       }
                       if(variables.get(c.getKey() + "_value") != null){
                           c.setDefaultValue((String) variables.get(c.getKey() + "_value"));
                       }
                       break;
                   case "checklist":
                       if(variables.get(c.getKey() + "_values") != null){
                           c.setValues((List<SelectOptionDto>) variables.get(c.getKey() + "_values"));
                       }
                       break;
                   case "radio":
                       if(variables.get(c.getKey() + "_values") != null){
                           c.setValues((List<SelectOptionDto>) variables.get(c.getKey() + "_values"));
                       }
                       break;
                   case "number":
                       if(variables.get(c.getKey() + "_value") != null){
                           c.setDefaultValue((Long) variables.get(c.getKey() + "_value"));
                       }
                       break;
                   case "checkbox":
                       if(variables.get(c.getKey() + "_value") != null){
                           c.setDefaultValue((Boolean) variables.get(c.getKey() + "_value"));
                       }
                       break;
                   default:
                       if(variables.get(c.getKey() + "_value") != null){
                           c.setDefaultValue((String) variables.get(c.getKey() + "_value"));
                       }
                       break;
               }


           components.add(c);
        }

        formDto.setComponents(components);

        // Construire la réponse
        return CamundaFormResponseDto.builder()
                .form(formDto)
                .metadata(metadata)
                .success(true)
                .message("Form retrieved successfully")
                .build();
    }

    /**
     * Récupère le formulaire de manière optionnelle
     *
     * @param taskId L'identifiant de la tâche
     * @return Optional contenant le formulaire ou vide si non trouvé
     */
    public Optional<CamundaFormResponseDto> getTaskFormOptional(String taskId) {
        try {
            return Optional.of(getTaskForm(taskId));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Vérifie si une tâche a un formulaire associé
     *
     * @param taskId L'identifiant de la tâche
     * @return true si un formulaire existe, false sinon
     */
    public boolean hasTaskForm(String taskId) {
        try {
            Task task = taskService.createTaskQuery()
                    .taskId(taskId)
                    .singleResult();

            if (task == null) {
                return false;
            }

            String formKey = formService.getTaskFormKey(
                    task.getProcessDefinitionId(),
                    task.getTaskDefinitionKey()
            );

            return formKey != null && !formKey.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Map<String, Object> getTaskFormVariables(String taskId) {
        return taskService.getVariablesTyped(taskId, true);
    }

    @Override
    public void completeTask(String taskId, Map<String, Object> variables) {
        // Créer une requête pour récupérer toutes les tâches associées à l'instance de processus
        taskService.complete(taskId, variables);
    }

}
