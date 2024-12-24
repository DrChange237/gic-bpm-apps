package com.ccabank.memoservice.process.mission.listener;

import com.ccabank.memoservice.dto.entity.AgencyInfo;
import com.ccabank.memoservice.dto.memo.ChoiceDto;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ApbtDirectorListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {

        String n1 = (String) delegateTask.getVariable("Apbt_Director"); // Remplacez par le nom de votre variable

        delegateTask.setAssignee(n1); // Remplacez "userId" par l'ID de l'utilisateur

        System.out.println("Assignation Directeur à " + n1);

    }
}
