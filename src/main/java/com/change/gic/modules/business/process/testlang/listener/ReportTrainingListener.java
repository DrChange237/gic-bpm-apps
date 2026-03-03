package com.change.gic.modules.business.process.testlang.listener;

import com.change.gic.exception.BadRequestException;
import com.change.gic.modules.business.entity.Contrat;
import com.change.gic.modules.business.entity.TestTraining;
import com.change.gic.modules.business.enumeration.ContratStatus;
import com.change.gic.modules.business.repository.ContratRepository;
import com.change.gic.modules.business.repository.TestTrainingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReportTrainingListener implements ExecutionListener {

    private final ContratRepository contratRepository;
    private final TestTrainingRepository testTrainingRepository;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        String reference = delegateExecution.getBusinessKey();
        Contrat contrat = contratRepository.findByReference(reference);
        if (!contrat.getStatus().equals(ContratStatus.PREPA_DOCUMENT)){
            throw  new BadRequestException("Ce contrat doit etre dans la phase de préparation de document");
        }

        Integer nbHour = (Integer) delegateExecution.getVariable("nbHour");
        Boolean levelup = (Boolean) delegateExecution.getVariable("levelup");
        String observation = (String) delegateExecution.getVariable("observation");

        TestTraining testTraining = new TestTraining();
        testTraining.setContract(contrat);
        testTraining.setNbHour(nbHour);
        testTraining.setObservation(observation);
        testTraining.setLevelup(levelup);
        testTrainingRepository.save(testTraining);

    }
}
