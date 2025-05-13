package com.ccabank.paperless.process.general.listener;

import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.entity.RequestStatus;
import com.ccabank.paperless.repository.RequestRepository;
import com.ccabank.paperless.service.faces.CamundaService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidationListener  implements TaskListener {

    @Autowired
    CamundaService camundaService;

    @Autowired
    RequestRepository requestRepository;

    @Override
    public void notify(DelegateTask delegateTask)  {
        // Logique à exécuter lors du démarrage du processus
        String processInstanceId = delegateTask.getProcessInstanceId();
        Request request = requestRepository.findByInstanceId(processInstanceId);
        if(request != null){
            request.setStatus(RequestStatus.DRAFT);
            requestRepository.save(request);
        }

    }

}
