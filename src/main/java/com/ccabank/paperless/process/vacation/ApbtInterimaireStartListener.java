package com.ccabank.paperless.process.vacation;

import com.ccabank.paperless.service.faces.CamundaService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ApbtInterimaireStartListener implements ExecutionListener {
    private final CamundaService camundaService;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        // Logique à exécuter lors du démarrage du processus

        String processInstanceId = execution.getProcessInstanceId();

        //String apbt_interimaire = (String) execution.getVariable("Apbt_interimaire");

        // Vous pouvez également accéder aux variables d'entrée
        //execution.setVariable("Apbt_interimaire", apbt_interimaire);
    }
}
