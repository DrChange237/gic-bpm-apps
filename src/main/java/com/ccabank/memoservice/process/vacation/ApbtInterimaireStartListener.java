package com.ccabank.memoservice.process.vacation;

import com.ccabank.memoservice.service.faces.CamundaService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ApbtInterimaireStartListener implements ExecutionListener {

    @Autowired
    CamundaService camundaService;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        // Logique à exécuter lors du démarrage du processus

        String processInstanceId = execution.getProcessInstanceId();

        //String apbt_interimaire = (String) execution.getVariable("Apbt_interimaire");

        // Vous pouvez également accéder aux variables d'entrée
        //execution.setVariable("Apbt_interimaire", apbt_interimaire);
    }
}
