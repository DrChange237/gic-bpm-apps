package com.ccabank.memoservice.process.general.implementation;

import com.ccabank.memoservice.dto.email.EmailAskApprovalDto;
import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.repository.RequestRepository;
import com.ccabank.memoservice.service.faces.CamundaService;
import com.ccabank.memoservice.service.faces.EmailService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendAskEmail implements JavaDelegate {

    protected String approbation = "Apbt_interimaire" ;

    @Autowired
    EmailService emailService;

    @Autowired
    CamundaService camundaService;

    @Autowired
    RequestRepository requestRepository;


    public String getApprobation() {
        return approbation;
    }

    public void setApprobation(String approbation) {
        this.approbation = approbation;
    }

    @Override
    public void execute(DelegateExecution execution) {


        EmailAskApprovalDto ask = new EmailAskApprovalDto();

        String processInstanceId = execution.getProcessInstanceId();


        String apbt_interimaire = (String) execution.getVariable(this.approbation);
        String owner = (String) execution.getVariable("owner");
        String reference = (String) execution.getVariable("reference");

        System.out.println("Approver : " + apbt_interimaire);

        System.out.println("Approbation : " + this.getApprobation());

        System.out.println("CamundaService  : " + camundaService);

        Task task = camundaService.getTaskByProcessInstanceIdAndTaskKey(processInstanceId, this.approbation);


        ask.setSender(owner);
        ask.setApprover(apbt_interimaire);
        ask.setReference(reference);

        Request request = requestRepository.findOneByReference(reference);

        ask.setType(request.getType().getName());

        ask.setSubject("Demande d'approbation");
        if(task != null){
            ask.setRole(task.getName());
        }else{
            ask.setRole("R.A.S");
        }


        emailService.sendAskApproval(ask);
        // Vous pouvez également définir des variables de sortie
        //execution.setVariable("outputVariable", "Processed: " + inputVariable);
    }

}
