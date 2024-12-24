package com.ccabank.memoservice.process.general.listener;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;


@Component
public class ApbtN1Listener implements TaskListener {


    @Override
    public void notify(DelegateTask delegateTask) {
        delegateTask.getBpmnModelElementInstance().getId();
        String n1 = (String) delegateTask.getVariable("Apbt_n1"); // Remplacez par le nom de votre variable

        delegateTask.setAssignee(n1); // Remplacez "userId" par l'ID de l'utilisateur

        System.out.println("Assignation de l'intérim à " + n1);

    }
}
