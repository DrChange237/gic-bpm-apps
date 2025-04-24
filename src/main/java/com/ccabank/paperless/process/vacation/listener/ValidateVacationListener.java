package com.ccabank.paperless.process.vacation.listener;

import com.ccabank.paperless.process.general.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ValidateVacationListener implements ExecutionListener {

    private final RequestService requestService;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {


    }
}
