package com.change.gic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class HttpClientException extends RuntimeException {
    public HttpClientException(String message) {
        super(message);
    }
}
