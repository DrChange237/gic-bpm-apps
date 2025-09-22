package com.ccabank.paperless.process.collect_signature.implementation;

import com.ccabank.paperless.service.faces.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class CSRejectDocument  implements JavaDelegate {

    private final EmailService emailService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Executing CSRejectDocument");
    }
}
