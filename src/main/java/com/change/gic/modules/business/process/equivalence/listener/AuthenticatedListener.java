package com.change.gic.modules.business.process.equivalence.listener;

import com.change.gic.exception.BadRequestException;
import com.change.gic.modules.business.entity.Contrat;
import com.change.gic.modules.business.entity.Equivalence;
import com.change.gic.modules.business.enumeration.EquivalenceStatus;
import com.change.gic.modules.business.repository.ContratRepository;
import com.change.gic.modules.business.repository.EquivalenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticatedListener implements ExecutionListener {

    private final EquivalenceRepository equivalenceRepository;
    private final ContratRepository contratRepository;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        String reference = delegateExecution.getBusinessKey();
        Contrat contrat = contratRepository.findByReference(reference);
        if (contrat == null) {
            throw  new BadRequestException("Contrat non trouv<UNK>");
        }
        Optional<Equivalence> equivalenceOptional = equivalenceRepository.findByContract(contrat);
        if (equivalenceOptional.isEmpty()) {
            throw  new BadRequestException("Equivalence non trouv<UNK>");
        }
        Equivalence equivalence = equivalenceOptional.get();
        equivalence.setStatus(EquivalenceStatus.ASK_AUTHENTICATED);
        equivalenceRepository.save(equivalence);
    }
}
