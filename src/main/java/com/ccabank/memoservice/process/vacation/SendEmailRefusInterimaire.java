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


        //Recupérer les infos sur le owner
        String owner = (String) execution.getVariable("owner");
        EmployeeInfo info = userRestClient.getStaffByUsername(owner);

        //Recupérer les infos sur l'intérimaire
        String interim = (String) execution.getVariable("Apbt_interimaire");
        EmployeeInfo interimInfo = userRestClient.getStaffByUsername(interim);

        // Récupérer le commentaire de refus
        String comment_Apbt_interimaire = (String) execution.getVariable("comment_Apbt_interimaire");


        EmailDto emailDto = new EmailDto();
        emailDto.setTo(info.getEmail());
        emailDto.setCc(interimInfo.getEmail());
        emailDto.setSubject("Refus Interimaire");
        emailDto.setBody(comment_Apbt_interimaire);
        emailDto.setFrom("notification@cca-bank.com");

        emailRestClient.send(emailDto);

        // Vous pouvez également définir des variables de sortie
        //execution.setVariable("outputVariable", "Processed: " + inputVariable);
    }

}
