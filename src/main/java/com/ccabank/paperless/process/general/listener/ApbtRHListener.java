package com.ccabank.paperless.process.general.listener;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApbtRHListener implements TaskListener {


    @Override
    public void notify(DelegateTask delegateTask) {

        String groupId = "capital-humain"; // Remplacez par l'ID de votre groupe
        delegateTask.addCandidateGroup(groupId);

        // Optionnel : log pour vérifier l'assignation
        log.info("Tâche assignée au groupe : " + groupId);

    }
}
