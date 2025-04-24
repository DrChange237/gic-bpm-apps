package com.ccabank.paperless.process.general.implementation;

import com.ccabank.paperless.service.faces.CamundaService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KillOthersTokens implements JavaDelegate {

    @Autowired
    private CamundaService camundaService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String processInstanceId = delegateExecution.getProcessInstanceId();
        delegateExecution.setVariable("fifo", "true");
    }
}
