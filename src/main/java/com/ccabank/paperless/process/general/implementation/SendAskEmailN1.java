package com.ccabank.paperless.process.general.implementation;

import org.springframework.stereotype.Component;

@Component
public class SendAskEmailN1 extends SendAskEmail {

    public SendAskEmailN1() {
        this.approbation = "Apbt_n1" ;
    }
}
