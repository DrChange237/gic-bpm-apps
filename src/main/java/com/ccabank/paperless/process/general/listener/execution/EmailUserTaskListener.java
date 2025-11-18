package com.ccabank.paperless.process.general.listener.execution;

import com.ccabank.paperless.dto.email.EmailAskApprovalDto;
import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.repository.RequestRepository;
import com.ccabank.paperless.service.faces.CamundaService;
import com.ccabank.paperless.service.faces.EmailService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EmailUserTaskListener implements ExecutionListener {

    private final EmailService emailService;
    private final CamundaService camundaService;
    private final RequestRepository requestRepository;

    @Override
    public void notify(DelegateExecution execution) throws Exception {

        log.info("EmailUserTaskListener Execution Listener");

        String processInstanceId = execution.getProcessInstanceId();

        String approbation = execution.getBpmnModelElementInstance().getId();

        log.info("ID: " + approbation);

        Task task = camundaService.getTaskByProcessInstanceIdAndTaskKey(processInstanceId, approbation);

        execution.getProcessEngineServices().getTaskService().createTaskQuery().taskId(execution.getId());

        //Task task = camundaService.getTaskDetails(taskId);

        if(task == null) {
            log.info("TaskId is null");
            return;
        }

        List<String> candidateUsers = camundaService.getAllAssigneInTask(task.getId());

        EmailAskApprovalDto ask = new EmailAskApprovalDto();

        String processDefinitionId = execution.getProcessDefinitionId();
        ProcessDefinition definition = camundaService.getProcessDefinition(processDefinitionId);
        String owner = (String) execution.getVariable("owner");
        String reference = (String) execution.getVariable("reference");
        ask.setSender(owner);
        ask.setReference(reference);
        Request request = requestRepository.findOneByReference(reference);
        ask.setType(request.getType().getName());

        ask.setSubject("Demande d'approbation - " + definition.getName());
        ask.setRole(task.getName());

        for (String user : candidateUsers) {
            log.info("Envoi de mail a " + user);
            ask.setApprover(user);
            emailService.sendAskApproval(ask);
        }

    }
}
