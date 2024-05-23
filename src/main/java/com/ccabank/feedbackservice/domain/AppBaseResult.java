package com.ccabank.feedbackservice.domain;

/**
 * @author : <a href="mailto:herve.foudjo@cca-bank.com">Herve FOUDJO</a>
 * @project : entityservice
 * @Package : com.ccabank.entityservice.domain
 * <p>
 * @date: 08/08/2023
 * @time: 10:22
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
public class AppBaseResult {

    private boolean success;
    private int errorCode;
    private String message;

    public AppBaseResult() {
    }

    public AppBaseResult(boolean success, int errorCode, String message) {
        this.success = success;
        this.errorCode = errorCode;
        this.message = message;
    }

    public static AppBaseResult GenarateIsSucceed() {
        return new AppBaseResult(true, 0, "Succeed!");
    }

    public static AppBaseResult GenarateIsFailed(int errorCode, String message) {
        return new AppBaseResult(false, errorCode, message);
    }

    public boolean isSuccess() {
        return success;
    }

    protected void setSuccess(boolean success) {
        this.success = success;
    }

    public int getErrorCode() {
        return errorCode;
    }

    protected void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    protected void setMessage(String message) {
        this.message = message;
    }

}
