package com.ccabank.paperless.process.vacation;


import com.ccabank.paperless.openfeign.EmailRestClient;
import com.ccabank.paperless.openfeign.UserRestClient;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendEmailRefusInterimaire implements JavaDelegate {

    @Autowired
    EmailRestClient emailRestClient;

    @Autowired
    UserRestClient userRestClient;

    @Override
    public void execute(DelegateExecution execution) {


        // Vous pouvez également définir des variables de sortie
        //execution.setVariable("outputVariable", "Processed: " + inputVariable);
    }

}
