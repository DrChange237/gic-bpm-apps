package com.ccabank.memoservice.process.absence.listener;

import com.ccabank.memoservice.dto.memo.ChoiceDto;
import com.ccabank.memoservice.process.absence.domain.ReasonAbsence;
import com.ccabank.memoservice.process.mission.constant.TransportCommonConstant;
import com.ccabank.memoservice.util.WorkDayCalculator;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class StartAbsenceListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        List<ChoiceDto> transport = new ArrayList<>();

        // Boucle sur l'enum avec for
        for (ReasonAbsence reasonAbsence : ReasonAbsence.values()) {
            ChoiceDto choice = new ChoiceDto(reasonAbsence.getName() + "(" + reasonAbsence.getNbDays() + " jours )", reasonAbsence.name());
            transport.add(choice);
        }
        delegateExecution.setVariable("reason" + "_choices" , transport);

    }

}
