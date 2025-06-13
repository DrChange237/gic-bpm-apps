package com.ccabank.paperless.process.mission.listener;

import com.ccabank.paperless.dto.memo.ChoiceDto;
import com.ccabank.paperless.process.mission.constant.TransportCommonConstant;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StartMissionListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        List<ChoiceDto> transport = new ArrayList<>();
        ChoiceDto choice = new ChoiceDto("Véhicule de Service", TransportCommonConstant.SERVICE_VEHICLE);
        transport.add(choice);
        choice = new ChoiceDto("Transport en Commun", TransportCommonConstant.COMMON_TRANSPORT);
        transport.add(choice);
        delegateExecution.setVariable("transport" + "_choices" , transport);

    }
}
