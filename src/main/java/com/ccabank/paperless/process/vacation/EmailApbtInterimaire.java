package com.ccabank.paperless.process.vacation;

import com.ccabank.paperless.dto.email.EmailAskApprovalDto;
import com.ccabank.paperless.service.faces.CamundaService;
import com.ccabank.paperless.service.faces.EmailService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailApbtInterimaire implements JavaDelegate {
    private final EmailService emailService;
    private final CamundaService camundaService;

    @Override
    public void execute(DelegateExecution execution) {

        String approbation = "Apbt_interimaire";

        EmailAskApprovalDto ask = new EmailAskApprovalDto();

        String processInstanceId = execution.getProcessInstanceId();


        String apbt_interimaire = (String) execution.getVariable(approbation);
        String owner = (String) execution.getVariable("owner");
        String reference = (String) execution.getVariable("reference");
        Task task = camundaService.getTaskByProcessInstanceIdAndTaskKey(processInstanceId, approbation);



        ask.setSender(owner);
        ask.setApprover(apbt_interimaire);
        ask.setReference(reference);
        ask.setType("Demande de Congés");
        ask.setSubject("Demande d'approbation pour Intérim");
        if(task != null){
            ask.setRole(task.getName());
        }


        emailService.sendAskApproval(ask);
        // Vous pouvez également définir des variables de sortie
        //execution.setVariable("outputVariable", "Processed: " + inputVariable);
    }

}

