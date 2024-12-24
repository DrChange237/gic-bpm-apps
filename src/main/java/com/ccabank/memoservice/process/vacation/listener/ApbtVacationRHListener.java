package com.ccabank.memoservice.process.vacation.listener;

import com.ccabank.memoservice.dto.memo.ChoiceDto;
import com.ccabank.memoservice.dto.reporting.InterimForm;
import com.ccabank.memoservice.process.absence.constant.BaseDeductionConstant;
import com.ccabank.memoservice.process.resumption.constant.ReasonConstant;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ApbtVacationRHListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {

        String groupId = "capital-humain"; // Remplacez par l'ID de votre groupe
        delegateTask.addCandidateGroup(groupId);

        // Optionnel : log pour vérifier l'assignation
        System.out.println("Tâche assignée au groupe : " + groupId);

        List<ChoiceDto> typeInterim = new ArrayList<>();

        ChoiceDto choice = new ChoiceDto("Intérim", InterimForm.Subject.INTERIM);
        typeInterim.add(choice);
        choice = new ChoiceDto("Continuité de service", InterimForm.Subject.CONTINUITY);
        typeInterim.add(choice);
        choice = new ChoiceDto("Pas d'intérim", InterimForm.Subject.NONE);
        typeInterim.add(choice);

        delegateTask.setVariable("typeInterim" + "_choices" , typeInterim);

    }
}
