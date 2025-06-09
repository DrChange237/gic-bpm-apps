package com.ccabank.paperless.process.resumption.listener;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartResumptionListener implements ExecutionListener {

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
