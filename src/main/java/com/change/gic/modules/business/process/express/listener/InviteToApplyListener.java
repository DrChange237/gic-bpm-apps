package com.change.gic.modules.business.process.express.listener;


import com.change.gic.exception.BadRequestException;
import com.change.gic.modules.business.entity.Contrat;
import com.change.gic.modules.business.entity.SelectionExpress;
import com.change.gic.modules.business.enumeration.SelectionExpressStatus;
import com.change.gic.modules.business.repository.ContratRepository;
import com.change.gic.modules.business.repository.SelectionExpressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class InviteToApplyListener implements ExecutionListener {

    private final SelectionExpressRepository selectionExpressRepository;
    private final ContratRepository contratRepository;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        String reference = delegateExecution.getBusinessKey();
        Contrat contrat = contratRepository.findByReference(reference);
        Optional<SelectionExpress> selectionExpressOptional = selectionExpressRepository.findByContract(contrat);
        if (selectionExpressOptional.isEmpty()) {
            throw new BadRequestException("selectionExpress not found");
        }
        SelectionExpress selectionExpress = selectionExpressOptional.get();
        selectionExpress.setContract(contrat);
        selectionExpress.setStatus(SelectionExpressStatus.SUCCESS);
        selectionExpressRepository.save(selectionExpress);
    }
}
