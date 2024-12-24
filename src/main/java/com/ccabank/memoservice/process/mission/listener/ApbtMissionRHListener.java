package com.ccabank.memoservice.process.mission.listener;

import com.ccabank.memoservice.dto.entity.AgencyInfo;
import com.ccabank.memoservice.dto.memo.ChoiceDto;
import com.ccabank.memoservice.openfeign.EntityRestClient;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ApbtMissionRHListener implements ExecutionListener {

    @Autowired
    private EntityRestClient entityRestClient;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {


        List<AgencyInfo> agencyInfos = entityRestClient.getAllAgencies();
        List<ChoiceDto> agencies = new ArrayList<>();


        for (AgencyInfo agencyInfo : agencyInfos) {
            ChoiceDto choiceDto = new ChoiceDto();
            choiceDto.setLabel(agencyInfo.getName());
            choiceDto.setValue(agencyInfo.getName());
            agencies.add(choiceDto);
        }

        delegateExecution.setVariable("chargeSupport" + "_choices" , agencies);

    }

}
