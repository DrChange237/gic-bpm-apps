package com.ccabank.entityservice.constant;

/**
 * @author : <a href="mailto:herve.foudjo@cca-bank.com">Herve FOUDJO</a>
 * @project : entity-service
 * @Package : com.ccabank.entityservice.constant
 * <p>
 * @date: 08/08/2023
 * @time: 10:05
 * <p>
 * Created with IntelliJ IDEA To change this template use File | Settings | File Templates.
 */
public enum AppError {

    Validattion(101, "Validation error"),
    Unknown(99, "Unknown error");

    private int errorCode;
    private String errorMessage;

    AppError(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int errorCode() {
        return errorCode;
    }

    public String errorMessage() {
        return errorMessage;
    }
}
