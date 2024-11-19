package com.ccabank.memoservice.process.general.listener;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;


@Component
public class ApbtN5Listener implements TaskListener {


    @Override
    public void notify(DelegateTask delegateTask) {
        String n5 = (String) delegateTask.getVariable("Apbt_n5"); // Remplacez par le nom de votre variable

        delegateTask.setAssignee(n5); // Remplacez "userId" par l'ID de l'utilisateur

        System.out.println("Assignation de l'intérim à " + n5);
    }
}
