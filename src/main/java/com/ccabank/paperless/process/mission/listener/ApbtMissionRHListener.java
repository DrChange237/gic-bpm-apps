package com.ccabank.paperless.process.mission.listener;

import com.ccabank.paperless.dto.user.AgencyDto;
import com.ccabank.paperless.dto.user.EmployeeInfo;
import com.ccabank.paperless.openfeign.UserRestClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApbtMissionRHListener implements ExecutionListener {

    private final UserRestClient userRestClient;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        String owner = (String) delegateExecution.getVariable("owner");
        EmployeeInfo staff =  userRestClient.getStaffByUsername(owner);
        log.info("set support charge to :{}", Optional.ofNullable(staff.getAgency()).map(AgencyDto::getName).orElse(null));
        delegateExecution.setVariable("supportCharge", Optional.ofNullable(staff.getAgency()).map(AgencyDto::getName).orElse(null));

    }

}
