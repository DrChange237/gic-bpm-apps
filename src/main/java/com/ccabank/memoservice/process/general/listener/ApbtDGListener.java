package com.ccabank.memoservice.process.general.listener;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

@Component
public class ApbtDGListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        // Assigner la tâche à un utilisateur spécifique
        String groupId = "direction-general"; // Remplacez par l'ID de votre groupe
        delegateTask.addCandidateGroup(groupId);

        groupId = "direction-general-adjoint"; // Remplacez par l'ID de votre groupe
        delegateTask.addCandidateGroup(groupId);

        // Optionnel : log pour vérifier l'assignation
        System.out.println("Tâche assignée au groupe : " + groupId);

    }
}
