package com.ccabank.paperless.process.purchase.listener;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class MGListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
         Long qty = (Long) delegateExecution.getVariable("qty");
         Long pu = (Long) delegateExecution.getVariable("pu");
         Long amount = pu * qty;
         delegateExecution.setVariable("amount", amount);
    }
}
