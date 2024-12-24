package com.ccabank.memoservice.process.general.listener;

import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.entity.RequestStatus;
import com.ccabank.memoservice.repository.RequestRepository;
import com.ccabank.memoservice.service.faces.CamundaService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EndRequestListener implements ExecutionListener {
    private final CamundaService camundaService;
    private final RequestRepository requestRepository;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        // Logique à exécuter lors du démarrage du processus
        String processInstanceId = execution.getProcessInstanceId();
        Request request = requestRepository.findByInstanceId(processInstanceId);
        request.setStatus(RequestStatus.ACCEPTED);
        requestRepository.save(request);
    }

}
