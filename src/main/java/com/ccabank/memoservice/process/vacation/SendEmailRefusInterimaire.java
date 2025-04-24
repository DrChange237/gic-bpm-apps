package com.ccabank.memoservice.process.vacation;


import com.ccabank.memoservice.dto.email.EmailDto;
import com.ccabank.memoservice.dto.user.EmployeeInfo;
import com.ccabank.memoservice.openfeign.EmailRestClient;
import com.ccabank.memoservice.openfeign.UserRestClient;
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
