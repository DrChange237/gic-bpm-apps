package com.ccabank.memoservice.process.mission.listener;

import com.ccabank.memoservice.dto.entity.AgencyInfo;
import com.ccabank.memoservice.dto.memo.ChoiceDto;
import com.ccabank.memoservice.dto.user.AgencyDto;
import com.ccabank.memoservice.dto.user.EmployeeInfo;
import com.ccabank.memoservice.openfeign.EntityRestClient;
import com.ccabank.memoservice.openfeign.UserRestClient;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ApbtMissionRHListener implements ExecutionListener {

    private final UserRestClient userRestClient;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        String owner = (String) delegateExecution.getVariable("owner");
        EmployeeInfo staff =  userRestClient.getStaffByUsername(owner);
        System.out.println("set support charge to :" + Optional.ofNullable(staff.getAgency()).map(AgencyDto::getName).orElse(null));
        delegateExecution.setVariable("supportCharge", Optional.ofNullable(staff.getAgency()).map(AgencyDto::getName).orElse(null));

    }

}
