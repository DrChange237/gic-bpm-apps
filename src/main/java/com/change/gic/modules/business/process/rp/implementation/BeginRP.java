package com.change.gic.modules.business.process.rp.implementation;

import com.change.gic.modules.business.entity.Contrat;
import com.change.gic.modules.business.entity.PermanentResident;
import com.change.gic.modules.business.enumeration.ContratStatus;
import com.change.gic.modules.business.enumeration.PermanentResidentStatus;
import com.change.gic.modules.business.repository.ContratRepository;
import com.change.gic.modules.business.repository.PermanentResidentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BeginRP implements JavaDelegate {

    private final ContratRepository contratRepository;
    private final PermanentResidentRepository permanentResidentRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        log.info("Begin Profil creation");
        String reference = (String) delegateExecution.getBusinessKey();
        Contrat contrat = contratRepository.findByReference(reference);

        PermanentResident permanentResident = new  PermanentResident();
        permanentResident.setContract(contrat);
        permanentResident.setStatus(PermanentResidentStatus.MEDICAL_VISIT);
        permanentResidentRepository.save(permanentResident);


        contrat.setStatus(ContratStatus.RESIDENCE_PERMANENT);
        contratRepository.save(contrat);
        delegateExecution.setVariable("if_permanent", true);
    }
}
