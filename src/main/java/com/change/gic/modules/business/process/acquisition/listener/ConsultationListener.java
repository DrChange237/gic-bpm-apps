package com.change.gic.modules.business.process.acquisition.listener;

import com.change.gic.modules.business.entity.Program;
import com.change.gic.modules.business.entity.TestLang;
import com.change.gic.modules.business.repository.ProgramRepository;
import com.change.gic.modules.business.repository.TestLangRepository;
import com.change.gic.modules.core.dto.camunda.form.SelectOptionDto;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultationListener implements ExecutionListener {

    private final ProgramRepository programRepository;
    private final TestLangRepository testLangRepository;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        List<Program> programs = programRepository.findAll();
        List<SelectOptionDto> selectOptionDtos = programs.stream().map(x -> new SelectOptionDto(x.getDescription(), x.getName())).collect(Collectors.toList());
        delegateExecution.setVariable("consultation_programs_values", selectOptionDtos);

        List<TestLang> testLangs = testLangRepository.findAll();
        selectOptionDtos = testLangs.stream().map(x -> new SelectOptionDto(x.getDescription(), x.getSlug())).collect(Collectors.toList());
        delegateExecution.setVariable("consultation_test_langs_values", selectOptionDtos);

    }
}
