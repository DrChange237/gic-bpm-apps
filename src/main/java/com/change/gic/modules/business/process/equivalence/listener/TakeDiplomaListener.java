package com.change.gic.modules.business.process.equivalence.listener;

import com.change.gic.modules.business.entity.Inscription;
import com.change.gic.modules.business.repository.InscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

@Component
@RequiredArgsConstructor
@Slf4j
public class TakeDiplomaListener implements ExecutionListener {

    private final InscriptionRepository inscriptionRepository;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        String reference = (String) delegateExecution.getBusinessKey();
        Inscription inscription = inscriptionRepository.findByReference(reference);
        delegateExecution.setVariable("fullname", inscription.getFullName());
        delegateExecution.setVariable("diploma", inscription.getDiploma());
        delegateExecution.setVariable("experience", inscription.getExperience());
        delegateExecution.setVariable("year_graduation", inscription.getYearGraduation());
        LocalDate birthday = inscription.getBirthday();
        int age = Period.between(birthday, LocalDate.now()).getYears();
        delegateExecution.setVariable("age", age);

    }
}
