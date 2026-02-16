package com.change.gic.modules.business.process.rp.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RpNotifMessage implements JavaDelegate {

    private final RuntimeService runtimeService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        String reference = (String) delegateExecution.getVariable("reference");

        runtimeService.createMessageCorrelation("Message_RP_Confirmed")
                .processInstanceBusinessKey(reference)
                .setVariable("validate", delegateExecution.getVariable("validate"))
                .correlate();
    }
}
