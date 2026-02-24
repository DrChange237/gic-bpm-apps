package com.change.gic.modules.business.process.equivalence.listener;

import com.change.gic.exception.BadRequestException;
import com.change.gic.modules.business.entity.Contrat;
import com.change.gic.modules.business.entity.Equivalence;
import com.change.gic.modules.business.enumeration.DiplomaStatus;
import com.change.gic.modules.business.enumeration.EquivalenceStatus;
import com.change.gic.modules.business.repository.ContratRepository;
import com.change.gic.modules.business.repository.EquivalenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BeginEquivalenceListener implements ExecutionListener {

    private final EquivalenceRepository equivalenceRepository;
    private final ContratRepository contratRepository;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        String reference = delegateExecution.getBusinessKey();
        Contrat contrat = contratRepository.findByReference(reference);
        if (contrat == null) {
            throw  new BadRequestException("Contrat non trouv<UNK>");
        }
        Equivalence equivalence = new Equivalence();
        equivalence.setContract(contrat);
        equivalence.setDiplomaStatus(DiplomaStatus.NONE);
        equivalenceRepository.save(equivalence);
    }
}
