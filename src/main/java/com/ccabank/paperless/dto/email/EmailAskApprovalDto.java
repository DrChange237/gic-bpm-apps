package com.ccabank.paperless.dto.email;

import lombok.Data;

@Data
public class EmailAskApprovalDto {

    private String subject;

    private String sender;

    private String message;


    private String bCC ;

    private  String cC;

    private String approver;

    private String reference;

    private String role;

    private String type;

    private AttachmentDto[] attachments;

}
