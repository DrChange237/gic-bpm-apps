package com.change.gic.modules.business.process.testlang.listener;

import com.change.gic.exception.BadRequestException;
import com.change.gic.modules.business.entity.Contrat;
import com.change.gic.modules.business.entity.Equivalence;
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
public class ContactStudentListenerCompleted implements ExecutionListener {

    private final ContratRepository contratRepository;
    private final TestExamRepository testExamRepository;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        String reference = delegateExecution.getBusinessKey();
        Contrat contrat = contratRepository.findByReference(reference);
        if (contrat == null) {
            throw  new BadRequestException("Contrat non trouv<UNK>");
        }
        String exam = (String) delegateExecution.getVariable("exam");
        TestExam testExam = new TestExam();
        testExam.setExam(exam);
        testExam.setContract(contrat);
        testExam.setStatus(TestExamStatus.CONTACT);
        testExamRepository.save(testExam);
        Boolean levelUp = (Boolean) delegateExecution.getVariable("level_up_exam");
        if (levelUp) {
            testExam.setStatus(TestExamStatus.LEVELUP);
            testExamRepository.save(testExam);
        }

    }
}
