package com.change.gic.modules.business.process.acquisition.implementation;

import com.change.gic.modules.business.entity.Consultation;
import com.change.gic.modules.business.entity.Inscription;
import com.change.gic.modules.business.entity.Program;
import com.change.gic.modules.business.entity.TestLang;
import com.change.gic.modules.business.enumeration.InscriptionStatus;
import com.change.gic.modules.business.repository.ConsultationRepository;
import com.change.gic.modules.business.repository.InscriptionRepository;
import com.change.gic.modules.business.repository.ProgramRepository;
import com.change.gic.modules.business.repository.TestLangRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

@Component
@Slf4j
@RequiredArgsConstructor
public class SaveConsultation implements JavaDelegate {

    private final TestLangRepository testLangRepository;
    private final ProgramRepository programRepository;
    private final ConsultationRepository consultationRepository;
    private final InscriptionRepository inscriptionRepository;

    @Override
    @Transactional
    public void execute(DelegateExecution delegateExecution) throws Exception {

        Consultation consultation = new Consultation();
        consultation.setEndDate(LocalDate.now());
        Boolean eligible = (Boolean) delegateExecution.getVariable("consultation_eligible");
        consultation.setEligible(eligible);

        String observation = (String) delegateExecution.getVariable("consultation_observations");
        consultation.setObservation(observation);

        Integer amountFirst = (Integer) delegateExecution.getVariable("consultation_amountFirst");
        consultation.setFirstAmount(BigDecimal.valueOf(amountFirst));

        Integer amountSecond = (Integer) delegateExecution.getVariable("consultation_amountSecond");
        consultation.setSecondAmount(BigDecimal.valueOf(amountSecond));

        Integer amountLast = (Integer) delegateExecution.getVariable("consultation_amountLast");
        consultation.setLastAmount(BigDecimal.valueOf(amountLast));


        ArrayList<String> charges = (ArrayList<String>) delegateExecution.getVariable("consultation_charges");
        log.info(charges.toString());
        if (charges.contains("charge_equivalence")){
            consultation.setEquivalence(true);
        }
        if (charges.contains("charge_test_lang")){
            consultation.setTestLang(true);
        }

        ArrayList<String> testLangs = (ArrayList<String> ) delegateExecution.getVariable("consultation_test_langs");
        log.info(testLangs.toString());
        for (String testLang : testLangs){
            TestLang test = testLangRepository.findBySlug(testLang);
            consultation.getTestLangs().add(test);
        }

        ArrayList<String> programs = (ArrayList<String> ) delegateExecution.getVariable("consultation_programs");
        log.info(programs.toString());
        for (String program : programs){
            Program p = programRepository.findByName(program);
            consultation.getPrograms().add(p);
        }

        Inscription inscription = (Inscription) delegateExecution.getVariable("inscription");
        inscription.setStatus(InscriptionStatus.CONSULTED);
        inscriptionRepository.save(inscription);
        log.info("Save Insciption");

        consultation.setInscription(inscription);
        consultationRepository.save(consultation);
        log.info("Save Consultation");
        delegateExecution.setVariable("consultation", consultation);
    }

}
