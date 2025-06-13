package com.ccabank.paperless.process.vacation;

import com.ccabank.paperless.service.faces.CamundaService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartEventListener implements ExecutionListener {

    @Autowired
    CamundaService camundaService;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        // Logique à exécuter lors du démarrage du processus

        String processInstanceId = execution.getProcessInstanceId();

        String apbt_interimaire = (String) execution.getVariable("Apbt_interimaire");
        Task task = camundaService.getTaskByProcessInstanceIdAndTaskKey(processInstanceId, "Apbt_interimaire");

        //task.setAssignee(apbt_interimaire);

        // Vous pouvez également accéder aux variables d'entrée
        //String someVariable = (String) execution.getVariable("someVariable");
    }

}
