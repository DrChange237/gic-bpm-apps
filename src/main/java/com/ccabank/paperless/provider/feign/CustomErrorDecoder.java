package com.ccabank.paperless.provider.feign;

import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder errorDecoder = new ErrorDecoder.Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 400:
                return new FeignException.BadRequest(response.reason() != null ? response.reason() : "Bad Request", response.request(), response.request().body());
            case 401:
                return new FeignException.Unauthorized(response.reason() != null ? response.reason() : "Unauthorized !", response.request(), response.request().body());
            case 403:
                return new FeignException.Forbidden(response.reason() != null ? response.reason() : "Forbidden !", response.request(), response.request().body());
            case 404:
                return new Exception();
            case 500:
                return new FeignException.InternalServerError(response.reason() != null ? response.reason() : "Unknown error !", response.request(), response.request().body());
            default:
                return new FeignException.InternalServerError(response.reason(), response.request(), response.request().body());
        }
    }
}
