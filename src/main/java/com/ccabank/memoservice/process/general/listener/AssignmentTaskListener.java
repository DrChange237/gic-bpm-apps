package com.ccabank.memoservice.process.general.listener;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

@Component
public class AssignmentTaskListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        String userTaskId = (String) delegateTask.getVariable(delegateTask.getBpmnModelElementInstance().getId()); // Remplacez par le nom de votre variable
        delegateTask.setAssignee(userTaskId); // Remplacez "userId" par l'ID de l'utilisateur
        System.out.println("Assignation de la tâche à " + userTaskId);
    }
}
