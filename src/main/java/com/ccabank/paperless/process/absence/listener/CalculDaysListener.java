package com.ccabank.paperless.process.absence.listener;

import com.ccabank.paperless.process.absence.domain.ReasonAbsence;
import com.ccabank.paperless.util.WorkDayCalculator;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
public class CalculDaysListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        String processInstanceId = delegateExecution.getProcessInstanceId();
        Date startDateD = (Date) delegateExecution.getVariable("startDate");
        LocalDate startDate = startDateD.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        Date endDateD = (Date) delegateExecution.getVariable("endDate");
        LocalDate endDate = endDateD.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();


        String reason = (String) delegateExecution.getVariable("reason");
        ReasonAbsence absenceReason = ReasonAbsence.valueOf(reason);

        int nbDays = (int) WorkDayCalculator.calculateWorkdays(startDate, endDate);

        delegateExecution.setVariable("nbDays", nbDays);


        if(!absenceReason.equals(ReasonAbsence.OTHER)){
            Long plusDays = (Long) delegateExecution.getVariable("plusDays");
            nbDays = absenceReason.getNbDays() + plusDays.intValue();
            endDate = WorkDayCalculator.addBusinessDays(startDate,  nbDays);
            delegateExecution.setVariable("endDate", endDate);
        }

        delegateExecution.setVariable("nbDays", nbDays);

    }

}
