package com.ccabank.paperless.process.mission.listener;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApbtDirectorListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {

        String n1 = (String) delegateTask.getVariable("Apbt_Director"); // Remplacez par le nom de votre variable

        delegateTask.setAssignee(n1); // Remplacez "userId" par l'ID de l'utilisateur

        log.info("Assignation Directeur à {}", n1);

    }
}
