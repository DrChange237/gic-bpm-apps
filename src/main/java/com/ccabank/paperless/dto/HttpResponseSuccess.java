package com.ccabank.paperless.dto;

import org.springframework.http.HttpStatus;

public class HttpResponseSuccess<T> extends HttpResponse {

    private T data;

    public HttpResponseSuccess(T data) {
        super(Boolean.TRUE, HttpStatus.OK);
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
