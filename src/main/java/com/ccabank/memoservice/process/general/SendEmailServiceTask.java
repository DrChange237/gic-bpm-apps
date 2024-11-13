package com.ccabank.memoservice.process.general;


import com.ccabank.memoservice.dto.email.EmailDto;
import com.ccabank.memoservice.openfeign.EmailRestClient;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendEmailServiceTask implements JavaDelegate {

    @Autowired
    EmailRestClient emailRestClient;

    @Override
    public void execute(DelegateExecution execution) {


        // Recupérer les infos sur l'intérimaire

        EmailDto emailDto = new EmailDto();



        // Récupérer la variable d'entrée
        String comment_Apbt_interimaire = (String) execution.getVariable("comment_Apbt_interimaire");

        System.out.println("SendEmailServiceTask Execution : " + comment_Apbt_interimaire);

        // Vous pouvez également définir des variables de sortie
        //execution.setVariable("outputVariable", "Processed: " + inputVariable);
    }

}
