package com.ccabank.memoservice.process.general.implementation;

import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.service.faces.CamundaService;
import com.ccabank.memoservice.service.impl.CamundaServiceImpl;
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
