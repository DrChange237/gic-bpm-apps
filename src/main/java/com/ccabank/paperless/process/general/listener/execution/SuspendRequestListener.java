package com.ccabank.paperless.process.general.listener.execution;

import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.entity.RequestStatus;
import com.ccabank.paperless.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class SuspendRequestListener  implements ExecutionListener {

    private final RequestRepository requestRepository;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        String processInstanceId = delegateExecution.getProcessInstanceId();
        Request request = requestRepository.findByInstanceId(processInstanceId);
        request.setStatus(RequestStatus.SUSPENDED);
        requestRepository.save(request);
    }
}
