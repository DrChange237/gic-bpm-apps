package com.ccabank.memoservice.process.resumption.listener;

import com.ccabank.memoservice.dto.memo.ChoiceDto;
import com.ccabank.memoservice.process.resumption.constant.ReasonConstant;
import com.ccabank.memoservice.service.faces.CamundaService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StartResumptionListener implements ExecutionListener {

    @Autowired
    CamundaService camundaService;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        /*String processInstanceId = delegateExecution.getProcessInstanceId();
        List<ChoiceDto> choices = new ArrayList<>();
        ChoiceDto choice = new ChoiceDto("Congés Annuels", ReasonConstant.ANNUAL);
        choices.add(choice);
        choice = new ChoiceDto("Maternité", ReasonConstant.MATERNITY);
        choices.add(choice);
        choice = new ChoiceDto("Médical", ReasonConstant.MEDICAL);
        choices.add(choice);
        choice = new ChoiceDto("Absence", ReasonConstant.ABSENCE);
        choices.add(choice);

        camundaService.setProcessVariable(processInstanceId, "reason" , choices);*/

    }
}
