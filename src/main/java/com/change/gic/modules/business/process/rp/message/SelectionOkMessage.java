package com.change.gic.modules.business.process.rp.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SelectionOkMessage implements JavaDelegate {

    private final RuntimeService runtimeService;


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String reference = delegateExecution.getBusinessKey();

        runtimeService.createMessageCorrelation("Message_Selection_OK")
                .processInstanceBusinessKey(reference)
                .setVariable("validate", delegateExecution.getVariable("validate"))
                .correlate();
    }
}
