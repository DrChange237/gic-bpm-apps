package com.ccabank.paperless.dto.email;

import lombok.Data;

@Data
public class EmailDto {

    private String to;
    private String cc;
    private String subject;
    private String body;

    private AttachmentDto[] attachments;
}
