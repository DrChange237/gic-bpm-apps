package com.ccabank.memoservice.process.general.listener;

import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.entity.RequestStatus;
import com.ccabank.memoservice.repository.RequestRepository;
import com.ccabank.memoservice.service.faces.CamundaService;
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
