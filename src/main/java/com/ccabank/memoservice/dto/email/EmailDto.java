package com.ccabank.memoservice.dto.email;

import java.util.List;

public class EmailDto {

    private String to;

    private String subject;

    private List<String> bCC;

    private String message;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<String> getbCC() {
        return bCC;
    }

    public void setbCC(List<String> bCC) {
        this.bCC = bCC;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
