package com.ccabank.memoservice.process.purchase.listener;

import com.ccabank.memoservice.dto.memo.ChoiceDto;
import com.ccabank.memoservice.process.purchase.constant.BudgetAvaibility;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ControlBudgetListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        List<ChoiceDto> budgets = new ArrayList<>();
        ChoiceDto choice = new ChoiceDto("Mensuel", BudgetAvaibility.MENSUEL);
        budgets.add(choice);
        choice = new ChoiceDto("Annuel", BudgetAvaibility.ANNUEL);
        budgets.add(choice);
        choice = new ChoiceDto("Disponible", BudgetAvaibility.AVAILABLE);
        budgets.add(choice);

        delegateExecution.setVariable("budget_choices" , budgets);


    }
}
