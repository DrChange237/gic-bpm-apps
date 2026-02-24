package com.change.gic.modules.business.process.equivalence.listener;

import com.change.gic.exception.BadRequestException;
import com.change.gic.modules.business.entity.Contrat;
import com.change.gic.modules.business.entity.Equivalence;
import com.change.gic.modules.business.enumeration.DiplomaStatus;
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
public class EquivalenceProfilCompletedListener implements ExecutionListener {

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
        String organism = (String) delegateExecution.getVariable("organism_equivalence");
        String numero = (String) delegateExecution.getVariable("num_org_equivalence");
        equivalence.setOrganisme(organism);
        equivalence.setRef(numero);
        equivalenceRepository.save(equivalence);
    }
}
