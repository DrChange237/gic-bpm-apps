package com.ccabank.paperless.process.vacation.listener;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ValidateVacationListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {


    }
}
