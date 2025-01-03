package com.ccabank.memoservice.process.absence.listener;

import com.ccabank.memoservice.dto.memo.ChoiceDto;
import com.ccabank.memoservice.dto.reporting.AbsenceForm;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class RHThreadListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        String processInstanceId = delegateExecution.getProcessInstanceId();
        List<ChoiceDto> baseDeductions = new ArrayList<>();
        ChoiceDto choice = new ChoiceDto("Congés", AbsenceForm.Deduction.VACATION);
        baseDeductions.add(choice);
        choice = new ChoiceDto("Salaire", AbsenceForm.Deduction.SALARY);
        baseDeductions.add(choice);

        delegateExecution.setVariable("deduction_choices" , baseDeductions);

    }
}
