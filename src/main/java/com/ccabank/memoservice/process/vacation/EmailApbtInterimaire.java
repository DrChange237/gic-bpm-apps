package com.ccabank.memoservice.process.vacation;

import com.ccabank.memoservice.dto.email.EmailAskApprovalDto;
import com.ccabank.memoservice.dto.email.EmailDto;
import com.ccabank.memoservice.dto.user.EmployeeInfo;
import com.ccabank.memoservice.openfeign.EmailRestClient;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.service.faces.CamundaService;
import com.ccabank.memoservice.service.faces.EmailService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailApbtInterimaire implements JavaDelegate {

    @Autowired
    EmailService emailService;

    @Autowired
    CamundaService camundaService;

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

