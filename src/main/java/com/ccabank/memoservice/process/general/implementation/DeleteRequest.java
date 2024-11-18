package com.ccabank.memoservice.process.general.implementation;

import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.entity.RequestStatus;
import com.ccabank.memoservice.repository.RequestRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class DeleteRequest implements JavaDelegate {

    private RequestRepository requestRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        String processInstanceId = delegateExecution.getProcessInstanceId();
        Request request = requestRepository.findByInstanceId(processInstanceId);
        request.setArchived(true);
        requestRepository.save(request);
    }
}
