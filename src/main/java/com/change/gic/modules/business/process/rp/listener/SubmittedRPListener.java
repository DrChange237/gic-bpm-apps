package com.change.gic.modules.business.process.rp.listener;

import com.change.gic.exception.BadRequestException;
import com.change.gic.modules.business.entity.Contrat;
import com.change.gic.modules.business.entity.PermanentResident;
import com.change.gic.modules.business.enumeration.PermanentResidentStatus;
import com.change.gic.modules.business.repository.ContratRepository;
import com.change.gic.modules.business.repository.PermanentResidentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubmittedRPListener implements ExecutionListener {

    private final ContratRepository contratRepository;
    private final PermanentResidentRepository permanentResidentRepository;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        String reference = delegateExecution.getBusinessKey();
        Contrat contrat = contratRepository.findByReference(reference);
        Optional<PermanentResident> permanentResidentOptional = permanentResidentRepository.findByContract(contrat);
        if (permanentResidentOptional.isEmpty()) {
            throw new BadRequestException("permanent resident not found");
        }
        PermanentResident permanentResident = permanentResidentOptional.get();
        permanentResident.setStatus(PermanentResidentStatus.SUBMITTED);
        permanentResidentRepository.save(permanentResident);
    }
}
