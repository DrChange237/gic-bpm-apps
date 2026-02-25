package com.change.gic.modules.business.process.arrima.listener;

import com.change.gic.exception.BadRequestException;
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
@RequiredArgsConstructor
@Slf4j
public class ReceivedCSQListener implements ExecutionListener {

    private final SelectionArrimaRepository selectionArrimaRepository;
    private final ContratRepository contratRepository;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        String reference = delegateExecution.getBusinessKey();
        Contrat contrat = contratRepository.findByReference(reference);
        Optional<SelectionArrima> selectionArrimaOptional = selectionArrimaRepository.findByContract(contrat);
        if (selectionArrimaOptional.isEmpty()) {
            throw new BadRequestException("Selection arrima non trouv<UNK>");
        }
        SelectionArrima selectionArrima = selectionArrimaOptional.get();
        selectionArrima.setContract(contrat);
        selectionArrima.setStatus(SelectionArrimaStatus.SUCCESS);
        selectionArrimaRepository.save(selectionArrima);
    }
}
