package com.change.gic.modules.business.process.testlang.listener;

import com.change.gic.modules.business.entity.Inscription;
import com.change.gic.modules.business.entity.TestLang;
import com.change.gic.modules.business.entity.TestNotation;
import com.change.gic.modules.business.enumeration.Matiere;
import com.change.gic.modules.business.repository.InscriptionRepository;
import com.change.gic.modules.business.repository.TestLangRepository;
import com.change.gic.modules.core.dto.camunda.form.SelectOptionDto;
import com.change.gic.modules.core.repository.TestNotationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SelectTypeTestListener implements ExecutionListener {

    private final TestLangRepository testLangRepository;
    private final TestNotationRepository testNotationRepository;
    private final InscriptionRepository inscriptionRepository;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        String reference = (String) delegateExecution.getBusinessKey();
        Inscription inscription = inscriptionRepository.findByReference(reference);
        delegateExecution.setVariable("experience", inscription.getExperience());
        delegateExecution.setVariable("year_graduation", inscription.getYearGraduation());


        String test  = (String) delegateExecution.getVariable("type_test");
        TestLang testLang = testLangRepository.findBySlug(test);
        List<SelectOptionDto> options = new ArrayList<>();

        List<TestNotation> testNotationsEE = testNotationRepository.findByExamAndMatiereOrderByLevelDesc(testLang, Matiere.EE);
        for (TestNotation testNotation : testNotationsEE) {
            SelectOptionDto selectOptionDto = new SelectOptionDto();
            selectOptionDto.setLabel(testNotation.getLabel());
            selectOptionDto.setValue(testNotation.getLabel());
            options.add(selectOptionDto);
        }
        delegateExecution.setVariable("note_ee_values", options);

        options = new ArrayList<>();
        List<TestNotation> testNotationsEO = testNotationRepository.findByExamAndMatiereOrderByLevelDesc(testLang, Matiere.EO);
        for (TestNotation testNotation : testNotationsEO) {
            SelectOptionDto selectOptionDto = new SelectOptionDto();
            selectOptionDto.setLabel(testNotation.getLabel());
            selectOptionDto.setValue(testNotation.getLabel());
            options.add(selectOptionDto);
        }
        delegateExecution.setVariable("note_eo_values", options);

        options = new ArrayList<>();
        List<TestNotation> testNotationsCE = testNotationRepository.findByExamAndMatiereOrderByLevelDesc(testLang, Matiere.CE);
        for (TestNotation testNotation : testNotationsCE) {
            SelectOptionDto selectOptionDto = new SelectOptionDto();
            selectOptionDto.setLabel(testNotation.getLabel());
            selectOptionDto.setValue(testNotation.getLabel());
            options.add(selectOptionDto);
        }
        delegateExecution.setVariable("note_ce_values", options);

        options = new ArrayList<>();
        List<TestNotation> testNotationsCO = testNotationRepository.findByExamAndMatiereOrderByLevelDesc(testLang, Matiere.CO);
        for (TestNotation testNotation : testNotationsCO) {
            SelectOptionDto selectOptionDto = new SelectOptionDto();
            selectOptionDto.setLabel(testNotation.getLabel());
            selectOptionDto.setValue(testNotation.getLabel());
            options.add(selectOptionDto);
        }
        delegateExecution.setVariable("note_co_values", options);

    }
}
