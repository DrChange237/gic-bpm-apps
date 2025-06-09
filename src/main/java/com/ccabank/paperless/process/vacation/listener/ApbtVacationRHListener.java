package com.ccabank.paperless.process.vacation.listener;

import com.ccabank.paperless.dto.memo.ChoiceDto;
import com.ccabank.paperless.dto.reporting.InterimForm;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ApbtVacationRHListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        List<ChoiceDto> typeInterim = new ArrayList<>();

        ChoiceDto choice = new ChoiceDto("Intérim", InterimForm.Subject.INTERIM);
        typeInterim.add(choice);
        choice = new ChoiceDto("Continuité de service", InterimForm.Subject.CONTINUITY);
        typeInterim.add(choice);
        choice = new ChoiceDto("Pas d'intérim", InterimForm.Subject.NONE);
        typeInterim.add(choice);

        delegateExecution.setVariable("typeInterim" + "_choices" , typeInterim);

        List<ChoiceDto> complementary = new ArrayList<>();

        choice = new ChoiceDto("Oui", true);
        complementary.add(choice);
        choice = new ChoiceDto("Non", false);
        complementary.add(choice);
        delegateExecution.setVariable("complementary" + "_choices" , complementary);

    }
}
