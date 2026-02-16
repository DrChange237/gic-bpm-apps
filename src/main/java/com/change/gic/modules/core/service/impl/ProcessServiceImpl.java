package com.change.gic.modules.core.service.impl;

import com.change.gic.exception.NotFoundException;
import com.change.gic.modules.core.dto.camunda.ProcessStartResponse;
import com.change.gic.modules.core.dto.camunda.ProcessStepDto;
import com.change.gic.modules.core.entity.Module;
import com.change.gic.modules.core.info.ProcessInfo;
import com.change.gic.modules.core.entity.Process;
import com.change.gic.modules.core.mappers.ProcessMapper;
import com.change.gic.modules.core.repository.ModuleRepository;
import com.change.gic.modules.core.repository.ProcessRepository;
import com.change.gic.modules.core.service.faces.AuthService;
import com.change.gic.modules.core.service.faces.CamundaService;
import com.change.gic.modules.core.service.faces.ProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProcessServiceImpl implements ProcessService {

    private final ProcessRepository processRepository;
    private final ProcessMapper processMapper;
    private final ModuleRepository moduleRepository;
    private final CamundaService camundaService;
    private final AuthService  authService;
    private final HistoryService historyService;

    /**
     * Retourne les détails d'une étape spécifique
     * @param processInstanceId L'ID de l'instance de processus
     * @param activityId L'ID de l'activité
     * @return Liste des occurrences de cette activité
     */
    public List<ProcessStepDto> getStepsByActivityId(String processInstanceId, String activityId) {
        List<HistoricActivityInstance> activities = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .activityId(activityId)
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .list();

        return activities.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retourne uniquement les étapes terminées
     * @param processInstanceId L'ID de l'instance de processus
     * @return Liste des étapes terminées
     */
    public List<ProcessStepDto> getCompletedSteps(String processInstanceId) {
        List<HistoricActivityInstance> activities = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .finished()
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .list();

        return activities.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Mappe une HistoricActivityInstance vers un DTO
     */
    private ProcessStepDto mapToDTO(HistoricActivityInstance activity) {
        ProcessStepDto dto = new ProcessStepDto();
        dto.setActivityId(activity.getActivityId());
        dto.setActivityName(activity.getActivityName());
        dto.setActivityType(activity.getActivityType());
        dto.setStartTime(activity.getStartTime());
        dto.setEndTime(activity.getEndTime());
        dto.setDurationInMillis(activity.getDurationInMillis());
        dto.setAssignee(activity.getAssignee());
        dto.setTaskId(activity.getTaskId());
        dto.setExecutionId(activity.getExecutionId());
        return dto;
    }



    /**
     * Démarre un processus avec données du formulaire et utilisateur initiateur
     */
    @Override
    public ProcessStartResponse startProcessWithInitiator(String processDefinitionKey,
                                                          Map<String, Object> formData,
                                                          String businessKey) {
        return camundaService.startProcessWithInitiator(processDefinitionKey, formData, businessKey, authService.getCurrentUsername());
    }


    @Override
    public List<ProcessInfo> processListByModule(String moduleId) {
        String username = authService.getCurrentUsername();

        Module module = moduleRepository.findById(moduleId).orElseThrow(null);

        if (module == null) {
            throw new NotFoundException("Module not found");
        }

        List<Process> processList = processRepository.findByModule(module);
        List<Process> processAuth = new ArrayList<>();
        for (Process process : processList) {
            log.info(process.getKey());
            log.info(username);
            if(camundaService.canUserStartProcess(process.getKey(), username)) {
                processAuth.add(process);
            }
        }

        return processMapper.toDto(processList);
    }

    @Override
    public List<ProcessInfo> processList() {
        List<Process> processList = processRepository.findAll();
        return processMapper.toDto(processList);
    }
}
