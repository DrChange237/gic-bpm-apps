package com.ccabank.datareferenceservice.dto;

import org.springframework.http.HttpStatus;

/**
 * @author : <a href="mailto:patrick.simo@cca-bank.com">Patrick SIMO</a>
 * @project : user-service
 * @Package : com.ccabank.userservice.dto
 * <p>
 * @date: 19/06/2023
 * @time: 09:09
 * <p>
 * Created with IntelliJ IDEA To change this template use File | Settings | File Templates.
 */
public class HttpResponseError extends HttpResponse {

    // `private String reason;` is declaring a private instance variable of type String named `reason` in
    // the `HttpResponseError` class. This variable is used to store the reason for the error that
    // occurred.
    private String reason;

    // `private String errorMessage;` is declaring a private instance variable of type String named
    // `errorMessage` in the `HttpResponseError` class. This variable is used to store the error message
    // that describes the error that occurred.
    private String errorMessage;

    /**
     * Construct
     *
     * @param reason
     * @param errorMessage
     */
    public HttpResponseError(String reason, String errorMessage) {
        super(Boolean.FALSE, HttpStatus.BAD_REQUEST);
        this.reason = reason;
        this.errorMessage = errorMessage;
    }

    /**
     * Construct
     *
     * @param httpStatus
     * @param reason
     * @param errorMessage
     */
    public HttpResponseError(HttpStatus httpStatus, String reason, String errorMessage) {
        super(Boolean.FALSE, httpStatus);
        this.reason = reason;
        this.errorMessage = errorMessage;
    }

    /**
     * The function returns a string representing the reason.
     *
     * @return The method is returning a String value, which is the value of the variable "reason".
     */
    public String getReason() {
        return reason;
    }

    /**
     * This function sets the reason for a certain action or event.
     *
     * @param reason The parameter "reason" is a String type variable that is used to set the reason
     *               for a certain action or decision in a program. The "setReason" method takes in a
     *               String argument and assigns it to the instance variable "reason" of the current
     *               object.
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * This function returns the error message as a string.
     *
     * @return The method is returning a String variable called "errorMessage".
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * This function sets the error message for a Java object.
     *
     * @param errorMessage The parameter "errorMessage" is a String variable that represents an error
     *                     message that can be set for an object or class. The method
     *                     "setErrorMessage" sets the value of this variable to the provided input
     *                     value.
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
