package com.ccabank.memoservice.process.absence.listener;

import com.ccabank.memoservice.dto.memo.ChoiceDto;
import com.ccabank.memoservice.dto.reporting.AbsenceForm;
import com.ccabank.memoservice.process.absence.constant.BaseDeductionConstant;
import com.ccabank.memoservice.process.absence.domain.ReasonAbsence;
import com.ccabank.memoservice.process.resumption.constant.ReasonConstant;
import com.ccabank.memoservice.service.faces.CamundaService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
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
