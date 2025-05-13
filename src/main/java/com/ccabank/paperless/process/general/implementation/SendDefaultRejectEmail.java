package com.ccabank.paperless.process.general.implementation;

import com.ccabank.paperless.dto.email.EmailAskApprovalDto;
import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.repository.RequestRepository;
import com.ccabank.paperless.service.faces.CamundaService;
import com.ccabank.paperless.service.faces.EmailService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendDefaultRejectEmail implements JavaDelegate {

    @Autowired
    CamundaService camundaService;

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    EmailService emailService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        EmailAskApprovalDto ask = new EmailAskApprovalDto();

        String apbt_interimaire = (String) execution.getVariable("owner");
        String owner = (String) execution.getVariable("owner");
        String reference = (String) execution.getVariable("reference");


        ask.setSender(owner);
        ask.setApprover(apbt_interimaire);
        ask.setReference(reference);

        Request request = requestRepository.findOneByReference(reference);

        ask.setType(request.getType().getName());

        ask.setSubject("Refus d'approbation");

        emailService.sendRejectedApproval(ask);

    }
}
