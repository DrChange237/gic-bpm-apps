package com.change.gic.modules.business.process.arrima.listener;

import com.change.gic.modules.business.entity.Contrat;
import com.change.gic.modules.business.entity.SelectionArrima;
import com.change.gic.modules.business.enumeration.SelectionArrimaStatus;
import com.change.gic.modules.business.repository.ContratRepository;
import com.change.gic.modules.business.repository.SelectionArrimaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class CreateInteretArrimaListener implements ExecutionListener {

    private final SelectionArrimaRepository selectionArrimaRepository;
    private final ContratRepository contratRepository;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        String reference = delegateExecution.getBusinessKey();
        Contrat contrat = contratRepository.findByReference(reference);
        SelectionArrima selectionArrima = new SelectionArrima();
        selectionArrima.setContract(contrat);
        selectionArrima.setStatus(SelectionArrimaStatus.INTERET);
        selectionArrimaRepository.save(selectionArrima);
    }
}
