package com.ccabank.memoservice.process.general.listener;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

@Component
public class ApbtDAFListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        // Assigner la tâche à un utilisateur spécifique
        String groupId = "DAF"; // Remplacez par l'ID de votre groupe
        delegateTask.addCandidateGroup(groupId);

        // Optionnel : log pour vérifier l'assignation
        System.out.println("Tâche assignée au groupe : " + groupId);

    }
}
