package com.change.gic.modules.business.process.tirage.listener;

import com.change.gic.modules.business.entity.Tirage;
import com.change.gic.modules.business.repository.TirageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TirageListener implements ExecutionListener {

    private final TirageRepository tirageRepository;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        int pointsMin = Integer.parseInt(delegateExecution.getVariable("pointsMin").toString());
        int nb = Integer.parseInt(delegateExecution.getVariable("nb").toString());
        Tirage tirage = new Tirage();
        tirage.setPointMin(pointsMin);
        tirage.setNb(nb);
        tirageRepository.save(tirage);

    }
}
