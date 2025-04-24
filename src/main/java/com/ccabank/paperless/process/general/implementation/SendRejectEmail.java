package com.ccabank.paperless.process.general.implementation;

import com.ccabank.paperless.dto.email.EmailAskApprovalDto;
import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.process.general.service.RequestService;
import com.ccabank.paperless.repository.RequestRepository;
import com.ccabank.paperless.service.faces.CamundaService;
import com.ccabank.paperless.service.faces.EmailService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendRejectEmail implements JavaDelegate {

    protected String approbation = "Apbt_interimaire" ;

    @Autowired
    EmailService emailService;

    @Autowired
    CamundaService camundaService;

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    RequestService requestService;

    @Override
    public void execute(DelegateExecution execution) {

        requestService.rejectRequest(execution.getProcessInstanceId());

        EmailAskApprovalDto ask = new EmailAskApprovalDto();

        String processInstanceId = execution.getProcessInstanceId();


        String apbt_interimaire = (String) execution.getVariable(approbation);
        String owner = (String) execution.getVariable("owner");
        String reference = (String) execution.getVariable("reference");
        Task task = camundaService.getTaskByProcessInstanceIdAndTaskKey(processInstanceId, approbation);


        ask.setSender(owner);
        ask.setApprover(apbt_interimaire);
        ask.setReference(reference);

        Request request = requestRepository.findOneByReference(reference);

        ask.setType(request.getType().getName());

        ask.setSubject("Refus d'approbation");
        if(task != null){
            ask.setRole(task.getName());
        }


        emailService.sendRejectedApproval(ask);
        // Vous pouvez également définir des variables de sortie
        //execution.setVariable("outputVariable", "Processed: " + inputVariable);
    }
}
