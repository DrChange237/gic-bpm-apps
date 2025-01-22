package com.ccabank.memoservice.service.impl;


import com.ccabank.memoservice.dto.email.EmailAskApprovalDto;
import com.ccabank.memoservice.dto.email.EmailDto;
import com.ccabank.memoservice.dto.user.UserRestDto;
import com.ccabank.memoservice.openfeign.EmailRestClient;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.service.faces.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ccabank.memoservice.constant.BeanIdConstant.MEMO_SERVICE;

@Service
@Transactional
@Qualifier(MEMO_SERVICE)
public class EmailServiceImpl implements EmailService {

    @Autowired
    private EmailRestClient emailRestClient;

    @Autowired
    private UserRestClient userRestClient;

    @Override
    public boolean sendFiles(EmailAskApprovalDto ask){
        try{
            System.out.println("sendFiles-------------------------------------------------------------------------------------");

            UserRestDto sender = userRestClient.getAgencyByStaffUsername(ask.getSender(), "key", "secret");

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

            UserRestDto sender = userRestClient.getAgencyByStaffUsername(ask.getSender(), "key", "secret");
            UserRestDto approver = userRestClient.getAgencyByStaffUsername(ask.getApprover(), "key", "secret");

            System.out.println("Email :" + approver.getEmail());
            EmailDto emailDto = new EmailDto();

            emailDto.setTo(approver.getEmail());
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
                    "            <div class=\"sans-serif\" style=\"color: #969AA1; font-size: 18px; line-height: 28px; margin-bottom: 40px; text-align: center\">Bonjour M. <span>"+ approver.getName() +"</span>, <br>  Une demande d'approbation de document à été initié et est en attente</div>\n" +
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
    public boolean sendConfirmApproval(EmailAskApprovalDto ask){

        try {
                System.out.println("sendConfirmApproval");
                UserRestDto sender = userRestClient.getAgencyByStaffUsername(ask.getSender(), "key", "secret");

                String emailApprover = "";
                String nameApprover = "";

                UserRestDto approver = userRestClient.getAgencyByStaffUsername(ask.getApprover(), "key", "secret");
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

            UserRestDto sender = userRestClient.getAgencyByStaffUsername(ask.getSender(), "key", "secret");

            UserRestDto approver = userRestClient.getAgencyByStaffUsername(ask.getApprover(), "key", "secret");


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
    public boolean sendConfirmRequest(EmailAskApprovalDto ask){


        UserRestDto sender = userRestClient.getAgencyByStaffUsername(ask.getSender(), "key", "secret");

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
