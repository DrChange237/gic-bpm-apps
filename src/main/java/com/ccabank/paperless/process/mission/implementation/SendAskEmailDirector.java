package com.ccabank.paperless.process.mission.implementation;

import com.ccabank.paperless.process.general.implementation.SendAskEmail;
import org.springframework.stereotype.Component;

@Component
public class SendAskEmailDirector extends SendAskEmail {
    public SendAskEmailDirector() {
        this.approbation = "Apbt_Director" ;
    }
}
