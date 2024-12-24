package com.ccabank.memoservice.process.absence.listener;

import com.ccabank.memoservice.dto.memo.ChoiceDto;
import com.ccabank.memoservice.dto.reporting.AbsenceForm;
import com.ccabank.memoservice.util.WorkDayCalculator;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class CalculDaysListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        String processInstanceId = delegateExecution.getProcessInstanceId();
        LocalDate startDate = (LocalDate) delegateExecution.getVariable("startDate");
        LocalDate endDate = (LocalDate) delegateExecution.getVariable("endDate");

        long nbDays = WorkDayCalculator.calculateNights(startDate, endDate);

        delegateExecution.setVariable("nbDays", nbDays);

    }

}
