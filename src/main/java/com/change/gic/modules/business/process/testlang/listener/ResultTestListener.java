package com.change.gic.modules.business.process.testlang.listener;

import com.change.gic.exception.BadRequestException;
import com.change.gic.modules.business.entity.Contrat;
import com.change.gic.modules.business.entity.TestExam;
import com.change.gic.modules.business.entity.TestExamStatus;
import com.change.gic.modules.business.repository.ContratRepository;
import com.change.gic.modules.business.repository.TestExamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ResultTestListener implements ExecutionListener {

    private final ContratRepository contratRepository;
    private final TestExamRepository testExamRepository;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        String reference = delegateExecution.getBusinessKey();
        Contrat contrat = contratRepository.findByReference(reference);
        if (contrat == null) {
            throw  new BadRequestException("Contrat non trouv<UNK>");
        }
        Optional<TestExam> testExamOptional = testExamRepository.findByContract(contrat);
        if (testExamOptional.isEmpty()) {
            throw  new BadRequestException("Test Exam non trouv<UNK>");
        }
        TestExam testExam = testExamOptional.get();
        testExam.setStatus(TestExamStatus.EXECUTED);
        testExamRepository.save(testExam);
    }
}
