package com.change.gic.modules.business.process.versement.listener;

import com.change.gic.modules.business.entity.Contrat;
import com.change.gic.modules.business.repository.ContratRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BeforeTypeCashinListener implements ExecutionListener {

    private final ContratRepository contratRepository;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        String reference = (String) delegateExecution.getVariable("reference");
        boolean isFirst = false;
        if(reference == null){
            reference = delegateExecution.getBusinessKey();
            isFirst = true;
        }
        log.info(reference);
        delegateExecution.setVariable("reference_value", reference);
        Contrat contrat = contratRepository.findByReference(reference);
        log.info("contrat = {}", contrat);
        delegateExecution.setVariable("fullName_value", contrat.getConsultation().getInscription().getFullName());
        if(isFirst){
            delegateExecution.setVariable("amount_value", contrat.getFirstAmount().longValueExact());
        }

    }
}
