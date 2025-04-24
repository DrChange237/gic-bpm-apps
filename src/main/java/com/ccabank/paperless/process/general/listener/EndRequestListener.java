package com.ccabank.paperless.process.general.listener;

import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.entity.RequestStatus;
import com.ccabank.paperless.repository.RequestRepository;
import com.ccabank.paperless.service.faces.CamundaService;
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
