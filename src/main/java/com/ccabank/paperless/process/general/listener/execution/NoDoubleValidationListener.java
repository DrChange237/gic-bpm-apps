package com.ccabank.paperless.process.general.listener.execution;


import com.ccabank.paperless.entity.Approbation;
import com.ccabank.paperless.entity.ApprovalStatus;
import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.repository.ApprobationRepository;
import com.ccabank.paperless.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NoDoubleValidationListener implements ExecutionListener {

    private final ApprobationRepository approbationRepository;
    private final RequestRepository requestRepository;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        Request request = requestRepository.findByInstanceId(execution.getProcessInstanceId());
        List<Approbation> approbations = approbationRepository.findByReferenceAndStaffAndStatus(request.getReference(), userTaskId, ApprovalStatus.ACCEPTED);
        log.info(String.valueOf(approbations.size()));

        if (!approbations.isEmpty()) {
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            processEngine.getTaskService()
                    .createTaskQuery()
                    .processInstanceId(execution.getProcessInstanceId())
                    .taskDefinitionKey(execution.getCurrentActivityId())
                    .list()
                    .forEach(task -> processEngine.getTaskService().complete(task.getId()));
        }

    }
}
