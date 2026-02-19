package com.change.gic.modules.business.process.acquisition.listener;

import com.change.gic.modules.business.repository.InscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefineIDListener implements ExecutionListener {

    private final InscriptionRepository inscriptionRepository;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        long count = inscriptionRepository.countByYear(year);
        long sequence = count + 1;
        String reference = String.format("%02d%d%03d", month, year, sequence);
        delegateExecution.setProcessBusinessKey(reference);

    }
}
