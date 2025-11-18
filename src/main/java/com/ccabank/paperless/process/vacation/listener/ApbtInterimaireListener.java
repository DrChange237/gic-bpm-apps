package com.ccabank.paperless.process.vacation.listener;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

@Component
public class ApbtInterimaireListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        // Assigner la tâche à un utilisateur spécifique


        String interimaire = (String) delegateTask.getVariable("Apbt_interimaire"); // Remplacez par le nom de votre variable

        delegateTask.setAssignee(interimaire); // Remplacez "userId" par l'ID de l'utilisateur

        log.info("Assignation de l'intérim à " + interimaire);



    }
}
