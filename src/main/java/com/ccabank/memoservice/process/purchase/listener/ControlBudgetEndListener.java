package com.ccabank.memoservice.process.purchase.listener;

import com.ccabank.memoservice.process.purchase.constant.BudgetAvaibility;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class ControlBudgetEndListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        String avaibility = (String) delegateExecution.getVariable("budget");
        if(avaibility.equals(BudgetAvaibility.AVAILABLE)){
            delegateExecution.setVariable("budgetAvaibility", true);
        }

    }
}
