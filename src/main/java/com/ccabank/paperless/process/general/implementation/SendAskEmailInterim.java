package com.ccabank.paperless.process.general.implementation;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class SendAskEmailInterim extends SendAskEmail {

    public SendAskEmailInterim() {
        this.approbation = "Apbt_interimaire" ;
    }

}
