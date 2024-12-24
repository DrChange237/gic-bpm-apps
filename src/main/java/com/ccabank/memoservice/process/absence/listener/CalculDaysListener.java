package com.ccabank.memoservice.process.absence.listener;

import com.ccabank.memoservice.dto.memo.ChoiceDto;
import com.ccabank.memoservice.dto.reporting.AbsenceForm;
import com.ccabank.memoservice.process.absence.domain.ReasonAbsence;
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


        String reason = (String) delegateExecution.getVariable("reason");
        ReasonAbsence absenceReason = ReasonAbsence.valueOf(reason);

        int nbDays = WorkDayCalculator.calculateNights(startDate, endDate);
        if(!absenceReason.equals(ReasonAbsence.OTHER)){
            Long plusDays = (Long) delegateExecution.getVariable("plusDays");
            nbDays = absenceReason.getNbDays() + plusDays.intValue();
            endDate = WorkDayCalculator.addBusinessDays(startDate, nbDays);
            delegateExecution.setVariable("endDate", endDate);

        }
        delegateExecution.setVariable("nbDays", nbDays);

    }

}
