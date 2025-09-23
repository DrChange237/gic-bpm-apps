package com.ccabank.paperless.process.general.listener.task;

import com.ccabank.paperless.entity.Approbation;
import com.ccabank.paperless.entity.ApprovalStatus;
import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.repository.ApprobationRepository;
import com.ccabank.paperless.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class NoDoubleValidationListener implements TaskListener {


    private final RequestRepository requestRepository;
    private final ApprobationRepository approbationRepository;

    @Override
    public void notify(DelegateTask delegateTask) {

        Request request = requestRepository.findByInstanceId(delegateTask.getProcessInstanceId());
        List<Approbation> approbations = approbationRepository.findByReferenceAndStaffAndStatus(request.getReference(), delegateTask.getAssignee() , ApprovalStatus.ACCEPTED);
        log.info(String.valueOf(approbations.size()));

        if (!approbations.isEmpty()) {
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            processEngine.getTaskService()
                    .createTaskQuery()
                    .processInstanceId(delegateTask.getProcessInstanceId())
                    .taskDefinitionKey(delegateTask.getTaskDefinitionKey())
                    .list()
                    .forEach(task -> processEngine.getTaskService().complete(task.getId()));
        }
    }
}
