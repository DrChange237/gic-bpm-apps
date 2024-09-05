package com.ccabank.memoservice.service.impl;


import com.ccabank.memoservice.dto.email.EmailDto;
import com.ccabank.memoservice.dto.user.UserRestDto;
import com.ccabank.memoservice.entity.Approval;
import com.ccabank.memoservice.entity.ProcessUnity;
import com.ccabank.memoservice.entity.Request;
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
    public boolean sendAskApprovalUnity(Request request, Approval approval, ProcessUnity unity){

        System.out.println("sendAskApprovalUnity");

        UserRestDto sender = userRestClient.getAgencyByStaffUsername(request.getStaff(), "key", "secret");

        System.out.println("Email :" + unity.getStaffList());
        EmailDto emailDto = new EmailDto();

        emailDto.setTo(unity.getStaffList().replace(";",","));
        emailDto.setCc(sender.getEmail());
        emailDto.setFrom("notification@cca-bank.com");
        emailDto.setSubject("Demande d'approbation");


        emailDto.setBody("<table class=\"row\" align=\"center\" bgcolor=\"#F8F8F8\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\">\n" +
                "    <tr>\n" +
                "        <td class=\"spacer\" height=\"40\" style=\"line-height: 40px;\">&nbsp;</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <th class=\"column\" width=\"640\" style=\"padding-left: 30px; padding-right: 30px; font-weight: 400; text-align: left;\">\n" +
                "            <div class=\"sans-serif\" style=\"color: #969AA1; font-size: 24px; line-height: 28px; margin-bottom: 10px; text-align: center\"><strong>Demande d'approbation - <span>" + request.getType().getName() + "</span> </strong></div>\n" +
                "            <div class=\"sans-serif\" style=\"color: #969AA1; font-size: 18px; line-height: 28px; margin-bottom: 40px; text-align: center\">Bonjour M. <span>"+ unity.getName() +"</span>, <br>  Une demande d'approbation de document à été initié et est en attente</div>\n" +
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
                "                        <span>"+ request.getType().getName() +"</span>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"border-radius: 3px; text-align: right;\">\n" +
                "                        <span style=\"font-family: 'IBM Plex Sans', Arial, sans-serif; color: #333333; font-size: 14px; font-weight: 400; line-height: 15px; margin: 0px 0px 0px 0px;\">\n" +
                "                            <strong>Référence  </strong></span>\n" +
                "                    </td>\n" +
                "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"padding: 10px; border-radius: 3px; \">\n" +
                "                        <span>"+ request.getReference() +"</span>\n" +
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
                "                            <strong>Votre rôle  </strong> </span>\n" +
                "                    </td>\n" +
                "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"padding: 10px; border-radius: 3px;\">\n" +
                "                        <span>"+ approval.getRole() +"</span>\n" +
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


    @Override
    public boolean sendAskApproval(Request request, Approval approval){

        System.out.println("sendAskApproval");

        UserRestDto sender = userRestClient.getAgencyByStaffUsername(request.getStaff(), "key", "secret");
        UserRestDto approver = userRestClient.getAgencyByStaffUsername(approval.getStaff(), "key", "secret");

        System.out.println("Email :" + approver.getEmail());
        EmailDto emailDto = new EmailDto();

        emailDto.setTo(approver.getEmail());
        emailDto.setCc(sender.getEmail());
        emailDto.setFrom("notification@cca-bank.com");
        emailDto.setSubject("Demande d'approbation");


        emailDto.setBody("<table class=\"row\" align=\"center\" bgcolor=\"#F8F8F8\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\">\n" +
                "    <tr>\n" +
                "        <td class=\"spacer\" height=\"40\" style=\"line-height: 40px;\">&nbsp;</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <th class=\"column\" width=\"640\" style=\"padding-left: 30px; padding-right: 30px; font-weight: 400; text-align: left;\">\n" +
                "            <div class=\"sans-serif\" style=\"color: #969AA1; font-size: 24px; line-height: 28px; margin-bottom: 10px; text-align: center\"><strong>Demande d'approbation - <span>" + request.getType().getName() + "</span> </strong></div>\n" +
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
                "                        <span>"+ request.getType().getName() +"</span>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"border-radius: 3px; text-align: right;\">\n" +
                "                        <span style=\"font-family: 'IBM Plex Sans', Arial, sans-serif; color: #333333; font-size: 14px; font-weight: 400; line-height: 15px; margin: 0px 0px 0px 0px;\">\n" +
                "                            <strong>Référence  </strong></span>\n" +
                "                    </td>\n" +
                "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"padding: 10px; border-radius: 3px; \">\n" +
                "                        <span>"+ request.getReference() +"</span>\n" +
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
                "                            <strong>Votre rôle  </strong> </span>\n" +
                "                    </td>\n" +
                "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"padding: 10px; border-radius: 3px;\">\n" +
                "                        <span>"+ approval.getRole() +"</span>\n" +
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

    @Override
    public boolean sendConfirmApproval(Request request, Approval approval){


        System.out.println("sendConfirmApproval");
        UserRestDto sender = userRestClient.getAgencyByStaffUsername(request.getStaff(), "key", "secret");

        UserRestDto approver = userRestClient.getAgencyByStaffUsername(approval.getStaff(), "key", "secret");


        System.out.println("Email :" + approver.getEmail());



        EmailDto emailDto = new EmailDto();

        emailDto.setTo(sender.getEmail());
        emailDto.setCc(approver.getEmail());
        emailDto.setFrom("notification@cca-bank.com");
        emailDto.setSubject("Confirmation d'approbation");

        emailDto.setBody("<table class=\"row\" align=\"center\" bgcolor=\"#F8F8F8\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\">\n" +
                "    <tr>\n" +
                "        <td class=\"spacer\" height=\"40\" style=\"line-height: 40px;\">&nbsp;</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <th class=\"column\" width=\"640\" style=\"padding-left: 30px; padding-right: 30px; font-weight: 400; text-align: left;\">\n" +
                "            <div class=\"sans-serif\" style=\"color: #969AA1; font-size: 24px; line-height: 28px; margin-bottom: 10px; text-align: center\"><strong>Confirmation d'approbation - <span>"+ request.getType().getName() +"</span> </strong></div>\n" +
                "            <div class=\"sans-serif\" style=\"color: #969AA1; font-size: 18px; line-height: 28px; margin-bottom: 40px; text-align: center\">Bonjour M. <span>"+ sender.getName() +"</span>, <br> Votre demande d'approbation a été confimé par <strong><span> "+ approval.getRole() + "</span></p></strong> </div>\n" +
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
                "                        <span>"+ request.getType().getName() +"</span>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"border-radius: 3px; text-align: right;\">\n" +
                "                        <span style=\"font-family: 'IBM Plex Sans', Arial, sans-serif; color: #333333; font-size: 14px; font-weight: 400; line-height: 15px; margin: 0px 0px 0px 0px;\">\n" +
                "                            <strong>Référence  </strong></span>\n" +
                "                    </td>\n" +
                "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"padding: 10px; border-radius: 3px; \">\n" +
                "                        <span>"+ request.getReference() +"</span>\n" +
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
                "                <tr>\n" +
                "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"border-radius: 3px; text-align: right;\">\n" +
                "                        <span style=\"font-family: 'IBM Plex Sans', Arial, sans-serif; color: #333333; font-size: 14px; font-weight: 400; line-height: 15px; margin: 0px 0px 0px 0px;\">\n" +
                "                            <strong> Commentaires </strong> </span>\n" +
                "                    </td>\n" +
                "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"padding: 10px; border-radius: 3px;\">\n" +
                "                        <span>" + approval.getComments()  +  "</span>\n" +
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

    @Override
    public boolean sendRejectedApproval(Request request, Approval approval){

        System.out.println("sendRejectedApproval");

        UserRestDto sender = userRestClient.getAgencyByStaffUsername(request.getStaff(), "key", "secret");

        UserRestDto approver = userRestClient.getAgencyByStaffUsername(approval.getStaff(), "key", "secret");


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
                "            <div class=\"sans-serif\" style=\"color: #969AA1; font-size: 24px; line-height: 28px; margin-bottom: 10px; text-align: center\"><strong>Refus d'approbation - <span>"+ request.getType().getName() +"</span> </strong></div>\n" +
                "            <div class=\"sans-serif\" style=\"color: #969AA1; font-size: 18px; line-height: 28px; margin-bottom: 40px; text-align: center\">Bonjour M. <span>"+ approver.getName() +"</span>, <br> Votre demande d'approbation a été rejeté par <strong><span> "+ approval.getRole() + "</span></p></strong> </div>\n" +
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
                "                        <span>"+ request.getType().getName() +"</span>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"border-radius: 3px; text-align: right;\">\n" +
                "                        <span style=\"font-family: 'IBM Plex Sans', Arial, sans-serif; color: #333333; font-size: 14px; font-weight: 400; line-height: 15px; margin: 0px 0px 0px 0px;\">\n" +
                "                            <strong>Référence  </strong></span>\n" +
                "                    </td>\n" +
                "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"padding: 10px; border-radius: 3px; \">\n" +
                "                        <span>"+ request.getReference() +"</span>\n" +
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
                "                <tr>\n" +
                "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"border-radius: 3px; text-align: right;\">\n" +
                "                        <span style=\"font-family: 'IBM Plex Sans', Arial, sans-serif; color: #333333; font-size: 14px; font-weight: 400; line-height: 15px; margin: 0px 0px 0px 0px;\">\n" +
                "                            <strong> Commentaires </strong> </span>\n" +
                "                    </td>\n" +
                "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"padding: 10px; border-radius: 3px;\">\n" +
                "                        <span>" + approval.getComments()  +  "</span>\n" +
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

    @Override
    public boolean sendConfirmRequest(Request request){


        UserRestDto sender = userRestClient.getAgencyByStaffUsername(request.getStaff(), "key", "secret");

        String from = sender.getEmail() ;

        System.out.println("Email :" + sender.getEmail());

        EmailDto emailDto = new EmailDto();

        emailDto.setTo(sender.getEmail());
        emailDto.setFrom("notification@cca-bank.com");
        emailDto.setSubject(request.getType().getName() + " validé(e)");

        emailDto.setBody("<table class=\"row\" align=\"center\" bgcolor=\"#F8F8F8\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\">\n" +
                "    <tr>\n" +
                "        <td class=\"spacer\" height=\"40\" style=\"line-height: 40px;\">&nbsp;</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <th class=\"column\" width=\"640\" style=\"padding-left: 30px; padding-right: 30px; font-weight: 400; text-align: left;\">\n" +
                "            <div class=\"sans-serif\" style=\"color: #969AA1; font-size: 24px; line-height: 28px; margin-bottom: 10px; text-align: center\"><strong><span>"+ request.getType().getName() +"</span>  accordé(e) </strong></div>\n" +
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
                "                        <span>"+ request.getType().getName() +"</span>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"border-radius: 3px; text-align: right;\">\n" +
                "                        <span style=\"font-family: 'IBM Plex Sans', Arial, sans-serif; color: #333333; font-size: 14px; font-weight: 400; line-height: 15px; margin: 0px 0px 0px 0px;\">\n" +
                "                            <strong>Référence  </strong></span>\n" +
                "                    </td>\n" +
                "                    <td class=\"sans-serif\" bgcolor=\"#FFFFFF\" style=\"padding: 10px; border-radius: 3px; \">\n" +
                "                        <span>"+ request.getReference() +"</span>\n" +
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
