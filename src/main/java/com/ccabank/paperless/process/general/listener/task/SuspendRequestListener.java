package com.ccabank.paperless.process.general.listener.task;

import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.entity.RequestStatus;
import com.ccabank.paperless.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class SuspendRequestListener  implements TaskListener {

    private final RequestRepository requestRepository;

    @Override
    public void notify(DelegateTask delegateTask) {

        String processInstanceId = delegateTask.getProcessInstanceId();
        Request request = requestRepository.findByInstanceId(processInstanceId);
        request.setStatus(RequestStatus.SUSPENDED);
        requestRepository.save(request);

    }
}
