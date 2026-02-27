package com.change.gic.modules.business.process.collaborator.listener;

import com.change.gic.modules.business.entity.Agency;
import com.change.gic.modules.business.repository.AgencyRepository;
import com.change.gic.modules.core.dto.camunda.form.SelectOptionDto;
import com.change.gic.modules.core.entity.Role;
import com.change.gic.modules.core.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class BeforeCreateCollaboratorListener implements ExecutionListener {

    private final AgencyRepository agencyRepository;
    private final RoleRepository roleRepository;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        List<SelectOptionDto> selectOptionDtos = new ArrayList<>();
        List<Agency> agencies = agencyRepository.findAll();
        selectOptionDtos = agencies.stream().map(agency -> new SelectOptionDto(agency.getName(), agency.getId())).collect(Collectors.toList());
        delegateExecution.setVariable("agency_values", selectOptionDtos);

        List<Role> roles = roleRepository.findAll();
        selectOptionDtos = roles.stream().map(role -> new SelectOptionDto(role.getName(), role.getSlug())).collect(Collectors.toList());
        delegateExecution.setVariable("role_values", selectOptionDtos);

    }
}
