package com.ccabank.memoservice.process.general.listener;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

@Component
public class ApbtN3Listener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        String n3 = (String) delegateTask.getVariable("Apbt_n3"); // Remplacez par le nom de votre variable

        delegateTask.setAssignee(n3); // Remplacez "userId" par l'ID de l'utilisateur

        System.out.println("Assignation de l'intérim à " + n3);
    }
}
