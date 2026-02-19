package com.change.gic.modules.business.process.contract.implementation;

import com.change.gic.modules.business.entity.Contrat;
import com.change.gic.modules.business.enumeration.ContratStatus;
import com.change.gic.modules.business.repository.ContratRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EndRP implements JavaDelegate {

    private final ContratRepository contratRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        log.info("Begin Profil creation");
        String reference = (String) delegateExecution.getBusinessKey();
        Contrat contrat = contratRepository.findByReference(reference);
        contrat.setStatus(ContratStatus.FINISHED);
        contratRepository.save(contrat);
    }
}
