package com.change.gic.modules.business.process.versement.implementation;

import com.change.gic.modules.business.entity.MoneyMovement;
import com.change.gic.modules.business.enumeration.MovementFlow;
import com.change.gic.modules.business.repository.MoneyMovementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class SaveVersement implements JavaDelegate {

    private final MoneyMovementRepository moneyMovementRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        String reference = (String) delegateExecution.getVariable("reference");
        Integer amount = (Integer) delegateExecution.getVariable("amount");
        MoneyMovement moneyMovement = new MoneyMovement();
        moneyMovement.setReference(reference);
        moneyMovement.setFlow(MovementFlow.CREDIT);
        moneyMovement.setFees(true);
        moneyMovement.setAmount(BigDecimal.valueOf(amount));
        moneyMovement.setLabel("Versement pour procédure");
        moneyMovementRepository.save(moneyMovement);

    }
}
