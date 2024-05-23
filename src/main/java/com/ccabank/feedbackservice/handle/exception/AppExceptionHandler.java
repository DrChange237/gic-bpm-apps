package com.ccabank.feedbackservice.handle.exception;

import com.ccabank.feedbackservice.dto.HttpResponse;
import com.ccabank.feedbackservice.dto.HttpResponseError;
import com.ccabank.feedbackservice.provider.file.UnsupportedFileTypeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author : <a href="mailto:patrick.simo@cca-bank.com">Patrick SIMO</a>
 * @project : entity-service
 * @Package : com.ccabank.userservice.handle.exception
 * <p>
 * @date: 19/06/2023
 * @time: 09:15
 * <p>
 * Created with IntelliJ IDEA To change this template use File | Settings | File Templates.
 */
@RestControllerAdvice
public class AppExceptionHandler {

    private static final String ACCOUNT_LOCKED = "Vôtre compte a été bloqué. Veuillez contacter l'administrateur";
    private static final String METHOD_IS_NOT_ALLOWED = "Cette méthode de demande n'est pas autorisée sur ce point de terminaison. Veuillez envoyer une requête '%s'.";
    private static final String INTERNAL_SERVER_ERROR_MSG = "Une erreur s'est produite lors du traitement de la demande.";
    private static final String INCORRECT_CREDENTIALS = "Compte ou mot de passe incorrect. Veuillez réessayer.";
    private static final String ACCOUNT_DISABLED = "Votre compte n'a pas été activé. Veuillez confirmer l'e-mail ou contacter l'administrateur.";
    private static final String ERROR_PROCESSING_FILE = "Une erreur s'est produite lors du traitement du fichier.";
    private static final String NOT_ENOUGH_PERMISSION = "Vous n'avez pas les autorisations suffisantes pour y accéder.";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpResponse> methodArgumentNotValidException(
            MethodArgumentNotValidException exception) {

        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {

            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        String jsonErrors;
        try {
            jsonErrors = new ObjectMapper().writeValueAsString(errors);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());

            jsonErrors = "Invalid data";
        }
        return createHttpResponse(HttpStatus.BAD_REQUEST, jsonErrors);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<HttpResponse> methodNotSupportedException(
            HttpRequestMethodNotSupportedException exception) {
        HttpMethod supportedMethod = Objects.requireNonNull(exception.getSupportedHttpMethods())
                .iterator().next();
        return createHttpResponse(HttpStatus.METHOD_NOT_ALLOWED,
                String.format(METHOD_IS_NOT_ALLOWED, supportedMethod));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse> internalServerErrorException(Exception exception) {
        logger.error(exception.getMessage());
        return createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MSG);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<HttpResponse> iOException(IOException exception) {
        logger.error(exception.getMessage());
        return createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_PROCESSING_FILE);
    }

    @ExceptionHandler(UnsupportedFileTypeException.class)
    public ResponseEntity<HttpResponse> unsupportFileException(
            UnsupportedFileTypeException exception) {
        logger.error(exception.getMessage());
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(NoSuchFileException.class)
    public ResponseEntity<HttpResponse> noSuchFileException(NoSuchFileException exception) {
        logger.error(exception.getMessage());
        return createHttpResponse(HttpStatus.BAD_REQUEST, "File not found!");
    }

    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message) {
        HttpResponse httpResponse = new HttpResponseError(httpStatus, httpStatus.getReasonPhrase(),
                message);
        return new ResponseEntity<>(httpResponse, httpStatus);
    }
}
