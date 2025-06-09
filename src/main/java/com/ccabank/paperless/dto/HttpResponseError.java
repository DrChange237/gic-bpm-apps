package com.ccabank.paperless.dto;

import org.springframework.http.HttpStatus;


public class HttpResponseError extends HttpResponse {

    private String reason;

    private String errorMessage;

    public HttpResponseError(String reason, String errorMessage) {
        super(Boolean.FALSE, HttpStatus.BAD_REQUEST);
        this.reason = reason;
        this.errorMessage = errorMessage;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
