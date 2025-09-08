package com.ccabank.paperless.process.general.listener;

import com.ccabank.paperless.entity.Approbation;
import com.ccabank.paperless.entity.ApprovalStatus;
import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.repository.ApprobationRepository;
import com.ccabank.paperless.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AssignmentTaskListener implements TaskListener {


    private final ApprobationRepository approbationRepository;
    private final RequestRepository requestRepository;

    @Override
    public void notify(DelegateTask delegateTask) {


        String userTaskId = (String) delegateTask.getVariable(delegateTask.getBpmnModelElementInstance().getId()); // Remplacez par le nom de votre variable
        delegateTask.setAssignee(userTaskId); // Remplacez "userId" par l'ID de l'utilisateur
        System.out.println("Assignation de la tâche à " + userTaskId);

        Request request = requestRepository.findByInstanceId(delegateTask.getProcessInstanceId());

        List<Approbation> approbations = approbationRepository.findByReferenceAndStaffAndStatus(request.getReference(), userTaskId, ApprovalStatus.ACCEPTED);
        if (approbations.size() > 0) {
            delegateTask.setVariable("decision", true);
            delegateTask.complete();
        }

    }
}
