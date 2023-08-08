package com.ccabank.datareferenceservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.util.Date;

/**
 * @author : <a href="mailto:patrick.simo@cca-bank.com">Patrick SIMO</a>
 * @project : user-service
 * @Package : com.ccabank.userservice.dto
 * <p>
 * @date: 19/06/2023
 * @time: 09:06
 * <p>
 * Created with IntelliJ IDEA To change this template use File | Settings | File Templates.
 */
public abstract class HttpResponse {

    // `private Boolean success;` is declaring a private instance variable named `success` of type
    // `Boolean` in the abstract class `HttpResponse`. This variable is used to indicate whether the HTTP
    // response was successful or not. It can be set to `true` if the response was successful or `false`
    // if it was not.
    private Boolean success;

    // `private HttpStatus httpStatus;` is declaring a private instance variable named `httpStatus` of
    // type `HttpStatus` in the abstract class `HttpResponse`. This variable is used to store the HTTP
    // status of the response, such as `OK`, `NOT_FOUND`, `INTERNAL_SERVER_ERROR`, etc. It can be set
    // using the constructor of the class and retrieved using the `getHttpStatus()` method.
    private HttpStatus httpStatus;

    // `private int httpStatusCode;` is declaring a private instance variable named `httpStatusCode` of
    // type `int` in the abstract class `HttpResponse`. This variable is used to store the HTTP status
    // code of the response, such as `200`, `404`, `500`, etc. It can be set using the constructor of the
    // class and retrieved using the `getHttpStatusCode()` method.
    private int httpStatusCode;

    // `private Date timestamp = new Date();` is declaring a private instance variable named `timestamp`
    // of type `Date` in the abstract class `HttpResponse`. This variable is used to store the timestamp
    // of when the HTTP response was created. The `new Date()` initializes the `timestamp` variable with
    // the current date and time. The `@JsonFormat` annotation is used to format the `timestamp` variable
    // when it is serialized to JSON.
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date timestamp = new Date();

    public HttpResponse(boolean success, HttpStatus httpStatus) {
        this.success = success;
        this.httpStatus = httpStatus;
        this.httpStatusCode = httpStatus.value();
    }

    /**
     * This function returns a Boolean value indicating the success of an operation.
     *
     * @return A Boolean value is being returned.
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     * This function sets the value of a Boolean variable called "success".
     *
     * @param success The "success" parameter is a Boolean variable that represents whether an
     *                operation or task was successful or not. It can have two possible values: "true"
     *                if the operation was successful, or "false" if it was not. The method
     *                "setSuccess" is used to set the value of this
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    /**
     * This function returns an HTTP status.
     *
     * @return The method is returning an object of the HttpStatus class.
     */
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    /**
     * This function sets the HTTP status of an object.
     *
     * @param httpStatus The httpStatus parameter is an object of the HttpStatus class, which
     *                   represents the HTTP status code and message returned by a server in response
     *                   to a client's request. It contains information about the success or failure
     *                   of the request, as well as any additional details or error messages. The
     *                   setHttpStatus method is used
     */
    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    /**
     * The function returns an integer representing the HTTP status code.
     *
     * @return The method is returning an integer value which represents the HTTP status code.
     */
    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    /**
     * This function sets the HTTP status code for a Java object.
     *
     * @param httpStatusCode The httpStatusCode parameter is an integer value that represents the HTTP
     *                       status code of a response. HTTP status codes are three-digit numbers that
     *                       indicate the status of a client's request to a server. Examples of HTTP
     *                       status codes include 200 (OK), 404 (Not Found), and 500 (Internal
     */
    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    /**
     * The function returns a timestamp of type Date.
     *
     * @return The method is returning a Date object named "timestamp".
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * This function sets the timestamp value of an object to a given Date value.
     *
     * @param timestamp The parameter "timestamp" is a Date object that is being passed as an argument
     *                  to the method. The method sets the value of the instance variable "timestamp"
     *                  to the value of the passed Date object.
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
