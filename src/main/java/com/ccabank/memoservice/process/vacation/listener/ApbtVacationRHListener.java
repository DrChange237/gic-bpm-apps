package com.ccabank.memoservice.process.vacation.listener;

import com.ccabank.memoservice.dto.memo.ChoiceDto;
import com.ccabank.memoservice.dto.reporting.InterimForm;
import com.ccabank.memoservice.process.absence.constant.BaseDeductionConstant;
import com.ccabank.memoservice.process.resumption.constant.ReasonConstant;
import com.ccabank.memoservice.service.faces.CamundaService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ApbtVacationRHListener implements ExecutionListener {

    @Autowired
    private CamundaService camundaService;

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
