package com.change.gic.modules.business.process.tirage.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenerateTirageReport implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        log.info("Generate Tirage Report");

    }
}
