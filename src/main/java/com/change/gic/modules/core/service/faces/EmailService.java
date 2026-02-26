package com.change.gic.modules.core.service.faces;

public interface EmailService {
    void sendSimpleMail(String to, String subject, String text);

    void sendHtmlMail(String to, String subject, String body) throws Exception;
}
