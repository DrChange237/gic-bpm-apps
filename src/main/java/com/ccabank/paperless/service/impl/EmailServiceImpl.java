package com.ccabank.paperless.service.impl;


import com.ccabank.paperless.constant.FieldTypeConstant;
import com.ccabank.paperless.dto.email.EmailAskApprovalDto;
import com.ccabank.paperless.dto.email.EmailDto;
import com.ccabank.paperless.dto.memo.ApprovalDto;
import com.ccabank.paperless.dto.memo.FieldDto;
import com.ccabank.paperless.dto.user.UserRestDto;
import com.ccabank.paperless.entity.ApprovalKey;
import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.openfeign.EmailRestClient;
import com.ccabank.paperless.openfeign.UserRestClient;
import com.ccabank.paperless.service.faces.EmailService;
import com.ccabank.paperless.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.List;

import static com.ccabank.paperless.constant.BeanIdConstant.MEMO_SERVICE;

@Service
@Transactional
@Qualifier(MEMO_SERVICE)
public class EmailServiceImpl implements EmailService {

    @Autowired
    private EmailRestClient emailRestClient;

    @Autowired
    private UserRestClient userRestClient;

    @Value("${server_url}")
    private String server_url;


    @Override
    public boolean sendForValidation(ApprovalKey approvalKey, EmailAskApprovalDto ask, List<FieldDto> fields, List<ApprovalDto> approvalDtos){
        try{
            System.out.println("sendForValidation-------------------------------------------------------------------------------------");

            UserRestDto sender = userRestClient.getAgencyByStaffUsername(ask.getSender());
            UserRestDto approver = userRestClient.getAgencyByStaffUsername(ask.getApprover());


            EmailDto emailDto = new EmailDto();

            //emailDto.setCc(sender.getEmail());
            emailDto.setFrom("notification@cca-bank.com");
            emailDto.setSubject(ask.getSubject());
            emailDto.setTo(approver.getEmail());

            StringBuilder htmlContent = new StringBuilder();

            htmlContent.append("<html><head>")
                    .append("<style>")
                    .append("body { font-family: Arial, sans-serif; margin: 20px; }")
                    .append(".container { padding: 20px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); }")
                    .append("h2 { color: #333; }")
                    .append("ul { list-style-type: none; padding: 0; }")
                    .append("li { margin: 10px 0; }")
                    .append(".button { background-color: #683d98; color: white; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer; text-decoration: none; }")
                    .append(".button-refuse { background-color: #dc3545; }")
                    .append("</style>")
                    .append("</head><body>")
                    .append("<div class='container'>")
                    .append("<p> Bonjour M. " + approver.getName() + ", <br> <br> Ce message vous est envoyé automatiquement par <b>Paperless</b>. Nous vous prions de bien vouloir valider la demande suivante : </p>")
                    .append("<ul>");

            htmlContent.append("<li><strong>").append("Type").append(":</strong> ").append(ask.getType()).append("</li>");
            htmlContent.append("<li><strong>").append("Reference").append(":</strong> ").append(ask.getReference()).append("</li>");
            htmlContent.append("<li><strong>").append("Initiateur").append(":</strong> ").append(sender.getName()).append(" - " + sender.getFunction() + " - " + sender.getDepartment()).append("</li>");


            for (FieldDto field : fields) {
                if(field.getValue() != null){
                    if(field.getType().equals(FieldTypeConstant.FILE)){
                        htmlContent.append("<li><strong>").append(field.getName()).append(":</strong> <a href=").append(field.getValue()).append("> Télécharger ").append(" </a> </li>");
                    }if(field.getType().equals(FieldTypeConstant.DATE)){
                        // Définir le format souhaité
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        // Formater la date en chaîne
                        String formattedDate = formatter.format((DateUtil.convertToDate(field.getValue())));
                        htmlContent.append("<li><strong>").append(field.getName()).append(":</strong> ").append(formattedDate).append("</li>");
                    }
                    else{
                        htmlContent.append("<li><strong>").append(field.getName()).append(":</strong> ").append(field.getValue()).append("</li>");
                    }
                }
            }

            htmlContent.append("</ul>");

            htmlContent.append("<h3> Personnels ayant déjà approuvés </h3>");
            htmlContent.append("<ul>");


            for (ApprovalDto approvalDto : approvalDtos) {

                UserRestDto approver2 = userRestClient.getAgencyByStaffUsername(approvalDto.getStaff());
                htmlContent.append("<li>");

                htmlContent.append("<strong>").append(approvalDto.getRole()).append("  </strong> - ").append(approver2.getName() + " - ").append(approver2.getFunction() + "  ");

                /*if(approvalDto.getStatus().equals(ApprovalStatus.ACCEPTED)){
                    htmlContent.append("<strong>[ ACCEPTE ]</strong>");
                }else if (approvalDto.getStatus().equals(ApprovalStatus.REJECTED)){
                    htmlContent.append("<strong>[ REJETE ]</strong> Raison : " + approvalDto.getComments());
                }*/

                htmlContent.append("</li>");

            }

            htmlContent.append("</ul><br>");


            htmlContent.append("<a href='" + server_url + "/api/paperless/validationForm?key="+ approvalKey.getId() +"&taskId="+ approvalKey.getTaskId() +"&reference="+ approvalKey.getReference() +"' class='button'>Valider / Rejeter</a>")
                    .append("</div>")
                    .append("</body></html>");

            emailDto.setBody(htmlContent.toString());

            emailRestClient.send(emailDto);
        }catch (Exception e){
            System.out.println("Email Error" + e.getMessage());
        }

        return true;
    }

    @Override
    public boolean sendFiles(EmailAskApprovalDto ask){
        try{
            System.out.println("sendFiles-------------------------------------------------------------------------------------");

            UserRestDto sender = userRestClient.getAgencyByStaffUsername(ask.getSender());

            EmailDto emailDto = new EmailDto();
            emailDto.setTo(sender.getEmail());
            emailDto.setCc(ask.getbCC());
            emailDto.setFrom("notification@cca-bank.com");
            emailDto.setSubject(ask.getSubject());


            emailDto.setBody("<table class=\"row\" align=\"center\" bgcolor=\"#F8F8F8\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\">\n" +
                    "    <tr>\n" +
                    "        <td class=\"spacer\" height=\"40\" style=\"line-height: 40px;\">&nbsp;</td>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "        <th class=\"column\" width=\"640\" style=\"padding-left: 30px; padding-right: 30px; font-weight: 400; text-align: left;\">\n" +
                    "            <div class=\"sans-serif\" style=\"color: #969AA1; font-size: 18px; line-height: 28px; margin-bottom: 40px; text-align: center\">Bonjour M. <span>"+ sender.getName() +"</span>, <br> Votre document a été généré avec succès bien vouloir prendre connaissance  </div>\n" +
                    "            \n" +
                    "            \n" +
                    "            <div class=\"sans-serif\" style=\"color: #969AA1; font-size: 18px; line-height: 28px; margin-top: 20px; \">Bien vouloir vous connecter pour consulter cette demande</div>\n" +
                    "            <div style=\"color: #969AA1; font-size: 13px; margin-top: 30px;\">Merci, <br><strong>CCA BANK</strong></div>\n" +
                    "        </th>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "        <td class=\"spacer\" height=\"40\" style=\"line-height: 40px;\">&nbsp;</td>\n" +
                    "    </tr>\n" +
                    "</table>");


            emailDto.setAttachments(ask.getAttachments());
            emailRestClient.send(emailDto);
        }catch (Exception e){
            System.out.println("Email Error" + e.getMessage());
        }


        return true;
    }


    @Override
    public boolean sendAskApproval(EmailAskApprovalDto ask){
        try{
            System.out.println("sendAskApproval-------------------------------------------------------------------------------------");

            UserRestDto sender = userRestClient.getAgencyByStaffUsername(ask.getSender());
            UserRestDto approve = userRestClient.getAgencyByStaffUsername(ask.getApprover());

            System.out.println("Email :" + approve.getEmail());
            EmailDto emailDto = new EmailDto();

            emailDto.setTo(approve.getEmail());
            emailDto.setCc(sender.getEmail());
            emailDto.setFrom("notification@cca-bank.com");
            emailDto.setSubject(ask.getSubject());


            emailDto.setBody("<table class=\"row\" align=\"center\" bgcolor=\"#F8F8F8\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\">\n" +
                    "    <tr>\n" +
                    "        <td class=\"spacer\" height=\"40\" style=\"line-height: 40px;\">&nbsp;</td>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "        <th class=\"column\" width=\"640\" style=\"padding-left: 30px; padding-right: 30px; font-weight: 400; text-align: left;\">\n" +
                    "            <div class=\"sans-serif\" style=\"color: #969AA1; font-size: 24px; line-height: 28px; margin-bottom: 10px; text-align: center\"><strong>Demande d'approbation - <span>" + ask.getType() + "</span> </strong></div>\n" +
                    "            <div class=\"sans-serif\" style=\"color: #969AA1; font-size: 18px; line-height: 28px; margin-bottom: 40px; text-align: center\">Bonjour M. <span>"+ approve.getName() +"</span>, <br>  Une demande d'approbation de document à été initié et est en attente</div>\n" +
                    "            \n" +
                    "            \n" +
                    "            <table align=\"center\"  cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"margin: auto; word-break: break-all;\" role=\"presentation\">\n" +
                    "            \n" +
                    "                <tr>\n" +
                    "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"border-radius: 3px; text-align: right;\">\n" +
                    "                        <span style=\"font-family: 'IBM Plex Sans', Arial, sans-serif; color: #333333; font-size: 14px; font-weight: 400; line-height: 15px; margin: 0px 0px 0px 0px;\">\n" +
                    "                            <strong>Type de Document  </strong></span>\n" +
                    "                    </td>\n" +
                    "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"padding: 10px; border-radius: 3px; text-align: left; \">\n" +
                    "                        <span>"+ ask.getType() +"</span>\n" +
                    "                    </td>\n" +
                    "                </tr>\n" +
                    "                <tr>\n" +
                    "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"border-radius: 3px; text-align: right;\">\n" +
                    "                        <span style=\"font-family: 'IBM Plex Sans', Arial, sans-serif; color: #333333; font-size: 14px; font-weight: 400; line-height: 15px; margin: 0px 0px 0px 0px;\">\n" +
                    "                            <strong>Référence  </strong></span>\n" +
                    "                    </td>\n" +
                    "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"padding: 10px; border-radius: 3px; \">\n" +
                    "                        <span>"+ ask.getReference() +"</span>\n" +
                    "                    </td>\n" +
                    "                </tr>\n" +
                    "                <tr>\n" +
                    "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"border-radius: 3px; text-align: right;\">\n" +
                    "                        <span style=\"font-family: 'IBM Plex Sans', Arial, sans-serif; color: #333333; font-size: 14px; font-weight: 400; line-height: 15px; margin: 0px 0px 0px 0px;\">\n" +
                    "                            <strong>Initiateur  </strong> </span>\n" +
                    "                    </td>\n" +
                    "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"padding: 10px; border-radius: 3px;\">\n" +
                    "                        <span>"+ sender.getName() +"</span>\n" +
                    "                    </td>\n" +
                    "                </tr>\n" +
                    "                <tr>\n" +
                    "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"border-radius: 3px; text-align: right;\">\n" +
                    "                        <span style=\"font-family: 'IBM Plex Sans', Arial, sans-serif; color: #333333; font-size: 14px; font-weight: 400; line-height: 15px; margin: 0px 0px 0px 0px;\">\n" +
                    "                            <strong>Description  </strong> </span>\n" +
                    "                    </td>\n" +
                    "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"padding: 10px; border-radius: 3px;\">\n" +
                    "                        <span>"+ ask.getRole() +"</span>\n" +
                    "                    </td>\n" +
                    "                </tr>\n" +
                    "            </table>\n" +
                    "            <div class=\"sans-serif\" style=\"color: #969AA1; font-size: 18px; line-height: 28px; margin-top: 20px; \">Bien vouloir vous connecter pour consulter cette demande en <a href=\"https://applications.cca.ad/paperless/newRequest\">Cliquant ici</a> </div>\n" +
                    "            <div style=\"color: #969AA1; font-size: 13px; margin-top: 30px;\">Merci, <br><strong>CCA BANK</strong></div>\n" +
                    "        </th>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "        <td class=\"spacer\" height=\"40\" style=\"line-height: 40px;\">&nbsp;</td>\n" +
                    "    </tr>\n" +
                    "</table>");


            emailRestClient.send(emailDto);
        }catch (Exception e){
            System.out.println("Email Error" + e.getMessage());
        }


        return true;
    }

    @Override
    public boolean sendConfirmApproval(EmailAskApprovalDto ask){

        try {
            System.out.println("sendConfirmApproval");
            UserRestDto sender = userRestClient.getAgencyByStaffUsername(ask.getSender());

            String emailApprover = "";
            String nameApprover = "";

            UserRestDto approver = userRestClient.getAgencyByStaffUsername(ask.getApprover());
            System.out.println("Email :" + approver.getEmail());
            emailApprover = approver.getEmail();
            nameApprover = approver.getName();


            EmailDto emailDto = new EmailDto();

            emailDto.setTo(sender.getEmail());
            emailDto.setCc(emailApprover);
            emailDto.setFrom("notification@cca-bank.com");
            emailDto.setSubject("Confirmation d'approbation");

            emailDto.setBody("<table class=\"row\" align=\"center\" bgcolor=\"#F8F8F8\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\">\n" +
                    "    <tr>\n" +
                    "        <td class=\"spacer\" height=\"40\" style=\"line-height: 40px;\">&nbsp;</td>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "        <th class=\"column\" width=\"640\" style=\"padding-left: 30px; padding-right: 30px; font-weight: 400; text-align: left;\">\n" +
                    "            <div class=\"sans-serif\" style=\"color: #969AA1; font-size: 24px; line-height: 28px; margin-bottom: 10px; text-align: center\"><strong>Confirmation d'approbation - <span>"+ ask.getType() +"</span> </strong></div>\n" +
                    "            <div class=\"sans-serif\" style=\"color: #969AA1; font-size: 18px; line-height: 28px; margin-bottom: 40px; text-align: center\">Bonjour M. <span>"+ sender.getName() +"</span>, <br> Votre demande d'approbation a été confimé par <strong><span> "+ ask.getRole() + "</span></p></strong> </div>\n" +
                    "            \n" +
                    "            \n" +
                    "            <table align=\"center\"  cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"margin: auto; word-break: break-all;\" role=\"presentation\">\n" +
                    "            \n" +
                    "                <tr>\n" +
                    "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"border-radius: 3px; text-align: right;\">\n" +
                    "                        <span style=\"font-family: 'IBM Plex Sans', Arial, sans-serif; color: #333333; font-size: 14px; font-weight: 400; line-height: 15px; margin: 0px 0px 0px 0px;\">\n" +
                    "                            <strong>Type de Document  </strong></span>\n" +
                    "                    </td>\n" +
                    "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"padding: 10px; border-radius: 3px; text-align: left; \">\n" +
                    "                        <span>"+ ask.getType() +"</span>\n" +
                    "                    </td>\n" +
                    "                </tr>\n" +
                    "                <tr>\n" +
                    "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"border-radius: 3px; text-align: right;\">\n" +
                    "                        <span style=\"font-family: 'IBM Plex Sans', Arial, sans-serif; color: #333333; font-size: 14px; font-weight: 400; line-height: 15px; margin: 0px 0px 0px 0px;\">\n" +
                    "                            <strong>Référence  </strong></span>\n" +
                    "                    </td>\n" +
                    "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"padding: 10px; border-radius: 3px; \">\n" +
                    "                        <span>"+ ask.getReference() +"</span>\n" +
                    "                    </td>\n" +
                    "                </tr>\n" +
                    "                <tr>\n" +
                    "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"border-radius: 3px; text-align: right;\">\n" +
                    "                        <span style=\"font-family: 'IBM Plex Sans', Arial, sans-serif; color: #333333; font-size: 14px; font-weight: 400; line-height: 15px; margin: 0px 0px 0px 0px;\">\n" +
                    "                            <strong>Approbateur  </strong> </span>\n" +
                    "                    </td>\n" +
                    "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"padding: 10px; border-radius: 3px;\">\n" +
                    "                        <span>" + nameApprover +  "</span>\n" +
                    "                    </td>\n" +
                    "                </tr>\n" +
                    "            </table>\n" +
                    "            <div class=\"sans-serif\" style=\"color: #969AA1; font-size: 18px; line-height: 28px; margin-top: 20px; \">Bien vouloir vous connecter pour consulter cette demande</div>\n" +
                    "            <div style=\"color: #969AA1; font-size: 13px; margin-top: 30px;\">Merci, <br><strong>CCA BANK</strong></div>\n" +
                    "        </th>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "        <td class=\"spacer\" height=\"40\" style=\"line-height: 40px;\">&nbsp;</td>\n" +
                    "    </tr>\n" +
                    "</table>");






            emailRestClient.send(emailDto);
        }catch (Exception e){
            System.out.println("Email Error" + e.getMessage());
        }

        return true;
    }

    @Override
    public boolean sendRejectedApproval(EmailAskApprovalDto ask){
        try {

            System.out.println("sendRejectedApproval");

            UserRestDto sender = userRestClient.getAgencyByStaffUsername(ask.getSender());

            UserRestDto approver = userRestClient.getAgencyByStaffUsername(ask.getApprover());


            EmailDto emailDto = new EmailDto();

            emailDto.setTo(sender.getEmail());
            emailDto.setCc(approver.getEmail());
            emailDto.setFrom("notification@cca-bank.com");
            emailDto.setSubject("Refus d'approbation");

            emailDto.setBody("<table class=\"row\" align=\"center\" bgcolor=\"#F8F8F8\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\">\n" +
                    "    <tr>\n" +
                    "        <td class=\"spacer\" height=\"40\" style=\"line-height: 40px;\">&nbsp;</td>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "        <th class=\"column\" width=\"640\" style=\"padding-left: 30px; padding-right: 30px; font-weight: 400; text-align: left;\">\n" +
                    "            <div class=\"sans-serif\" style=\"color: #969AA1; font-size: 24px; line-height: 28px; margin-bottom: 10px; text-align: center\"><strong>Refus d'approbation - <span>"+ ask.getType() +"</span> </strong></div>\n" +
                    "            <div class=\"sans-serif\" style=\"color: #969AA1; font-size: 18px; line-height: 28px; margin-bottom: 40px; text-align: center\">Bonjour M. <span>"+ sender.getName() +"</span>, <br> Votre demande d'approbation a été rejeté  <strong><span> </span></p></strong> </div>\n" +
                    "            \n" +
                    "            \n" +
                    "            <table align=\"center\"  cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"margin: auto; word-break: break-all;\" role=\"presentation\">\n" +
                    "            \n" +
                    "                <tr>\n" +
                    "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"border-radius: 3px; text-align: right;\">\n" +
                    "                        <span style=\"font-family: 'IBM Plex Sans', Arial, sans-serif; color: #333333; font-size: 14px; font-weight: 400; line-height: 15px; margin: 0px 0px 0px 0px;\">\n" +
                    "                            <strong>Type de Document  </strong></span>\n" +
                    "                    </td>\n" +
                    "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"padding: 10px; border-radius: 3px; text-align: left; \">\n" +
                    "                        <span>"+ ask.getType() +"</span>\n" +
                    "                    </td>\n" +
                    "                </tr>\n" +
                    "                <tr>\n" +
                    "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"border-radius: 3px; text-align: right;\">\n" +
                    "                        <span style=\"font-family: 'IBM Plex Sans', Arial, sans-serif; color: #333333; font-size: 14px; font-weight: 400; line-height: 15px; margin: 0px 0px 0px 0px;\">\n" +
                    "                            <strong>Référence  </strong></span>\n" +
                    "                    </td>\n" +
                    "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"padding: 10px; border-radius: 3px; \">\n" +
                    "                        <span>"+ ask.getReference() +"</span>\n" +
                    "                    </td>\n" +
                    "                </tr>\n" +
                    "                <tr>\n" +
                    "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"border-radius: 3px; text-align: right;\">\n" +
                    "                        <span style=\"font-family: 'IBM Plex Sans', Arial, sans-serif; color: #333333; font-size: 14px; font-weight: 400; line-height: 15px; margin: 0px 0px 0px 0px;\">\n" +
                    "                            <strong>Approbateur  </strong> </span>\n" +
                    "                    </td>\n" +
                    "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"padding: 10px; border-radius: 3px;\">\n" +
                    "                        <span>" + approver.getName() +  "</span>\n" +
                    "                    </td>\n" +
                    "                </tr>\n" +
                    "            </table>\n" +
                    "            <div class=\"sans-serif\" style=\"color: #969AA1; font-size: 18px; line-height: 28px; margin-top: 20px; \">Bien vouloir vous connecter pour consulter cette demande</div>\n" +
                    "            <div style=\"color: #969AA1; font-size: 13px; margin-top: 30px;\">Merci, <br><strong>CCA BANK</strong></div>\n" +
                    "        </th>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "        <td class=\"spacer\" height=\"40\" style=\"line-height: 40px;\">&nbsp;</td>\n" +
                    "    </tr>\n" +
                    "</table>");






            emailRestClient.send(emailDto);
        }catch (Exception e){
            System.out.println("Email Error" + e.getMessage());
        }

        return true;
    }

    @Override
    public boolean sendSuspendRequest(Request request, String reason){

        UserRestDto sender = userRestClient.getAgencyByStaffUsername(request.getStaff());
        EmailDto emailDto = new EmailDto();

        emailDto.setTo(sender.getEmail());
        emailDto.setFrom("notification@cca-bank.com");
        emailDto.setSubject("Votre " + request.getType() + " a été suspendu(e)");
        emailDto.setBody("Vous avez initié une " + request.getType() + ", elle a été suspendu(e) : " + reason);

        try {
            emailRestClient.send(emailDto);
        }catch (Exception e){
            System.out.println("Email Error" + e.getMessage());
        }

        return true;
    }



    @Override
    public boolean sendConfirmRequest(EmailAskApprovalDto ask){


        UserRestDto sender = userRestClient.getAgencyByStaffUsername(ask.getSender());

        String from = sender.getEmail();

        System.out.println("Email :" + sender.getEmail());

        EmailDto emailDto = new EmailDto();

        emailDto.setTo(sender.getEmail());
        emailDto.setFrom("notification@cca-bank.com");
        emailDto.setSubject(ask.getType() + " validé(e)");

        emailDto.setBody("<table class=\"row\" align=\"center\" bgcolor=\"#F8F8F8\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\">\n" +
                "    <tr>\n" +
                "        <td class=\"spacer\" height=\"40\" style=\"line-height: 40px;\">&nbsp;</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <th class=\"column\" width=\"640\" style=\"padding-left: 30px; padding-right: 30px; font-weight: 400; text-align: left;\">\n" +
                "            <div class=\"sans-serif\" style=\"color: #969AA1; font-size: 24px; line-height: 28px; margin-bottom: 10px; text-align: center\"><strong><span>"+ ask.getType() +"</span>  accordé(e) </strong></div>\n" +
                "            <div class=\"sans-serif\" style=\"color: #969AA1; font-size: 18px; line-height: 28px; margin-bottom: 40px; text-align: center\">Bonjour M. <span>"+ sender.getName() +"</span>, <br> Votre demande  a été accordé </div>\n" +
                "            \n" +
                "            \n" +
                "            <table align=\"center\"  cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"margin: auto; word-break: break-all;\" role=\"presentation\">\n" +
                "            \n" +
                "                <tr>\n" +
                "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"border-radius: 3px; text-align: right;\">\n" +
                "                        <span style=\"font-family: 'IBM Plex Sans', Arial, sans-serif; color: #333333; font-size: 14px; font-weight: 400; line-height: 15px; margin: 0px 0px 0px 0px;\">\n" +
                "                            <strong>Type de Document  </strong></span>\n" +
                "                    </td>\n" +
                "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"padding: 10px; border-radius: 3px; text-align: left; \">\n" +
                "                        <span>"+ ask.getType() +"</span>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"border-radius: 3px; text-align: right;\">\n" +
                "                        <span style=\"font-family: 'IBM Plex Sans', Arial, sans-serif; color: #333333; font-size: 14px; font-weight: 400; line-height: 15px; margin: 0px 0px 0px 0px;\">\n" +
                "                            <strong>Référence  </strong></span>\n" +
                "                    </td>\n" +
                "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"padding: 10px; border-radius: 3px; \">\n" +
                "                        <span>"+ ask.getReference() +"</span>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "            </table>\n" +
                "            <div class=\"sans-serif\" style=\"color: #969AA1; font-size: 18px; line-height: 28px; margin-top: 20px; \">Bien vouloir vous connecter pour consulter cette demande</div>\n" +
                "            <div style=\"color: #969AA1; font-size: 13px; margin-top: 30px;\">Merci, <br><strong>CCA BANK</strong></div>\n" +
                "        </th>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td class=\"spacer\" height=\"40\" style=\"line-height: 40px;\">&nbsp;</td>\n" +
                "    </tr>\n" +
                "</table>");



        try {
            emailRestClient.send(emailDto);
        }catch (Exception e){
            System.out.println("Email Error" + e.getMessage());
        }

        return true;
    }



}
