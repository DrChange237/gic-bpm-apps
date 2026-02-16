package com.change.gic.modules.business.process.equivalence.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BackDiplomaMessage implements JavaDelegate {

    private final RuntimeService runtimeService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        String reference = (String) delegateExecution.getVariable("reference");
        runtimeService.createMessageCorrelation("Message_Back_Diploma")
                .processInstanceBusinessKey(reference)
                .setVariable("reason", delegateExecution.getVariable("reason"))
                .correlate();

    }
}
