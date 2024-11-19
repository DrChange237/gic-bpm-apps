package com.ccabank.memoservice.process.vacation.listener;

import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.entity.RequestStatus;
import com.ccabank.memoservice.process.general.service.RequestService;
import com.ccabank.memoservice.repository.RequestRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ValidateVacationListener implements ExecutionListener {

    @Autowired
    RequestService requestService;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        requestService.confirmRequest(delegateExecution.getProcessDefinitionId());

    }
}
