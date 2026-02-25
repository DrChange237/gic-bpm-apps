package com.change.gic.modules.business.process.versement.listener;

import com.change.gic.exception.BadRequestException;
import com.change.gic.modules.business.entity.Contrat;
import com.change.gic.modules.business.entity.Inscription;
import com.change.gic.modules.business.enumeration.InscriptionStatus;
import com.change.gic.modules.business.repository.ContratRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewCashinListener implements ExecutionListener {

    private final ContratRepository contratRepository;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        String reference = (String) delegateExecution.getVariable("contract_customerRef");
        Contrat contrat = contratRepository.findByReference(reference);
        if(contrat == null) {
            throw new BadRequestException("contrat reference does not exist");
        }
        delegateExecution.setProcessBusinessKey(reference);
    }
}
