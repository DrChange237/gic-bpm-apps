package com.change.gic.modules.business.process.testlang.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class VerifyExamTestMessage implements JavaDelegate {

    private final RuntimeService runtimeService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        String reference = (String) delegateExecution.getBusinessKey();

        runtimeService.createMessageCorrelation("Message_TestLang_Available")
                .processInstanceBusinessKey(reference)
                .setVariable("note_ee", delegateExecution.getVariable("note_ee"))
                .setVariable("note_eo", delegateExecution.getVariable("note_eo"))
                .setVariable("note_ce", delegateExecution.getVariable("note_ce"))
                .setVariable("note_co", delegateExecution.getVariable("note_co"))
                .setVariable("test_exam_ok", delegateExecution.getVariable("test_exam_ok"))
                .correlate();

    }
}
