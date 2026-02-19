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
@Slf4j
@RequiredArgsConstructor
public class BeginPrepaDoc implements JavaDelegate {

    private final ContratRepository contratRepository;


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Begin PrepaDoc");
        String reference = (String) delegateExecution.getBusinessKey();
        Contrat contrat = contratRepository.findByReference(reference);
        contrat.setStatus(ContratStatus.PREPA_DOCUMENT);
        contratRepository.save(contrat);
    }
}
