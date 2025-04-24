package com.ccabank.paperless.dto;

import org.springframework.http.HttpStatus;

/**
 * @author : <a href="mailto:patrick.simo@cca-bank.com">Patrick SIMO</a>
 * @project : entity-service
 * @Package : com.ccabank.userservice.dto
 * <p>
 * @date: 19/06/2023
 * @time: 09:13
 * <p>
 * Created with IntelliJ IDEA To change this template use File | Settings | File Templates.
 */
public class HttpResponseSuccess<T> extends HttpResponse {

    // `private T data;` is declaring a private instance variable `data` of type `T`. This variable is
    // used to store the data that will be returned in the response body of a successful HTTP response.
    // The type `T` is a generic type parameter, which means that the actual type of `data` will be
    // determined at runtime based on the type argument provided when creating an instance of
    // `HttpResponseSuccess`.
    private T data;

    /**
     * The response body of a successful HTTP response
     *
     * @param data
     */
    public HttpResponseSuccess(T data) {
        super(Boolean.TRUE, HttpStatus.OK);
        this.data = data;
    }

    /**
     * The function returns the value of a generic type variable called "data".
     *
     * @return The method is returning the value of the variable "data" of type T.
     */
    public T getData() {
        return data;
    }

    /**
     * This function sets the value of a variable called "data" to a given input.
     *
     * @param data "data" is a parameter of type T, which is a generic type. It represents the data
     *             that is being set in the current object. The "setData" method is used to set the
     *             value of this data parameter.
     */
    public void setData(T data) {
        this.data = data;
    }
}
