package com.change.gic.modules.business.process.rp.implementation;

import com.change.gic.exception.BadRequestException;
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

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class EndRP implements JavaDelegate {

    private final ContratRepository contratRepository;
    private final PermanentResidentRepository permanentResidentRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Begin Profil creation");
        String reference = (String) delegateExecution.getBusinessKey();
        Contrat contrat = contratRepository.findByReference(reference);
        Optional<PermanentResident> permanentResidentOptional = permanentResidentRepository.findByContract(contrat);
        if (permanentResidentOptional.isEmpty()) {
            throw new BadRequestException("permanent resident not found");
        }
        PermanentResident permanentResident = permanentResidentOptional.get();
        permanentResident.setStatus(PermanentResidentStatus.SUCCESS);
        permanentResidentRepository.save(permanentResident);
        contrat.setStatus(ContratStatus.FINISHED);
        contrat.setEndDate(LocalDate.now());
        contratRepository.save(contrat);
    }
}
