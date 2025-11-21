package com.ccabank.paperless.controller;


import com.ccabank.paperless.constant.MessageCode;
import com.ccabank.paperless.entity.bug.Bug;
import com.ccabank.paperless.exception.BadRequestException;
import com.ccabank.paperless.exception.HttpClientException;
import com.ccabank.paperless.exception.NotFoundException;
import com.ccabank.paperless.repository.BugRepository;
import com.ccabank.paperless.service.faces.CamundaService;
import com.ccabank.paperless.service.faces.EmailService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    private final EmailService emailService;

    private final BugRepository bugRepository;


    @Value("${server.error.include-stacktrace:never}")
    private String includeStackTrace;

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ApiError handleNotFoundException(NotFoundException exception, HttpServletRequest request){
        return new ApiError(HttpStatus.NOT_FOUND, exception, request);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(HttpClientException.class)
    public ApiError handleHttpClientException(HttpClientException exception, HttpServletRequest request){
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, exception, request);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ApiError handleValidationException(BadRequestException exception, HttpServletRequest request){
        ApiError response = new ApiError(HttpStatus.BAD_REQUEST, exception, request);
        response.setMessage(exception.getMessage());
        return response;
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(org.hibernate.exception.ConstraintViolationException.class)
    public ApiError handleSqlConstraintViolation(org.hibernate.exception.ConstraintViolationException exception, HttpServletRequest request){
        ApiError response = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, exception, request);
        response.setMessage(exception.getSQLException().getLocalizedMessage());
        return response;
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiError handleConstraintViolation(ConstraintViolationException exception, HttpServletRequest request){
        ApiError response = new ApiError(HttpStatus.BAD_REQUEST, exception, request);
        response.setMessage(messageSource.getMessage(MessageCode.CONSTRAINT_VIOLATION, null, request.getLocale()));
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        violations.forEach(violation -> response.putDetails(violation, messageSource, request));
        return response;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ApiError handleDataIntegrityViolation(DataIntegrityViolationException exception, HttpServletRequest request){
        ApiError response = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, exception, request);
        response.setMessage(NestedExceptionUtils.getMostSpecificCause(exception).getMessage());
        return response;
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception exception, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute("javax.servlet.error.exception", exception, 0);
        }
        return buildResponse(exception, body, headers, status, ((ServletWebRequest)request).getRequest());
    }

    private ResponseEntity<Object> buildResponse(Exception exception, Object body, HttpHeaders headers, HttpStatus status, HttpServletRequest request) {
        log.error(exception.getClass().getSimpleName() + " {}\n", request.getRequestURI(), exception);
        Bug bug = new Bug();
        bug.setLabel(exception.getMessage());
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        bug.setTraces(sw.toString());
        bugRepository.save(bug);
        emailService.sendBug(exception.getMessage(), sw.toString());
        if(body == null){
            ApiError response = new ApiError(status, exception, request);
            if(exception instanceof MethodArgumentNotValidException){
                List<FieldError> errors = ((MethodArgumentNotValidException) exception).getBindingResult().getFieldErrors();
                errors.forEach(error -> response.putDetails(error, messageSource, request));
                response.setMessage(messageSource.getMessage(MessageCode.METHOD_ARGUMENT_NOT_VALID, null, request.getLocale()));
            }else if(exception instanceof MethodArgumentTypeMismatchException){
                response.setMessage(messageSource.getMessage(MessageCode.METHOD_ARGUMENT_TYPE_MISMATCH, new Object[]{((MethodArgumentTypeMismatchException) exception).getName()}, request.getLocale()));
            }else if(exception instanceof HttpMessageNotReadableException){
                Throwable throwable = ((HttpMessageNotReadableException) exception).getMostSpecificCause();
                if(throwable instanceof InvalidFormatException){
                    Object type = ((InvalidFormatException) throwable).getTargetType().getSimpleName();
                    Object value = ((InvalidFormatException) throwable).getValue();
                    response.setMessage(messageSource.getMessage(MessageCode.INVALID_FORMAT_EXCEPTION, new Object[]{type, value}, request.getLocale()));
                } else {
                    response.setMessage(throwable.getMessage());
                }
            }
            if(!"never".equalsIgnoreCase(includeStackTrace)) response.setTrace(ExceptionUtils.getStackTrace(exception));
            body = response;
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        return new ResponseEntity(body, headers, status);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidationError {
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_ONLY)
        private String path;
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_ONLY)
        private String message;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ApiError {
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_ONLY)
        private Date timestamp = new Date();
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_ONLY)
        private int status;
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_ONLY)
        private String code;
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_ONLY)
        private String message;
        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        private String trace;
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_ONLY)
        private String path;
        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        private List<ValidationError> details;

        public ApiError(HttpStatus status, Exception exception, HttpServletRequest request) {
            this.status = status.value();
            this.code = getReadableCode(exception);
            this.message = exception.getMessage();
            this.path = request.getRequestURI();
        }

        public static String getReadableCode(Exception exception) {
            String text = exception.getClass().getSimpleName();
            Pattern WORD_FINDER = Pattern.compile("(([A-Z]?[a-z]+)|([A-Z]))");
            Matcher matcher = WORD_FINDER.matcher(text);
            List<String> words = new ArrayList<>();
            while (matcher.find()) words.add(matcher.group(0).toUpperCase());
            words.remove("EXCEPTION");
            return String.join("_", words);
        }

        public void putDetails(FieldError error, MessageSource messageSource, HttpServletRequest request){
            if(Objects.isNull(details)) details = new ArrayList<>();
            ValidationError violation = new ValidationError();
            violation.setPath(error.getField());
            violation.setMessage(messageSource.getMessage(StringUtils.defaultString(error.getDefaultMessage()), null, request.getLocale()));
            details.add(violation);
        }

        public void putDetails(ConstraintViolation<?> violation, MessageSource messageSource, HttpServletRequest request){
            if(Objects.isNull(details)) details = new ArrayList<>();
            ValidationError error = new ValidationError();
            String path = violation.getPropertyPath().toString();
            List<String> nodes = Arrays.stream(path.split("\\.")).collect(Collectors.toList());
            if(!nodes.isEmpty()) nodes.remove(0);
            error.setPath(String.join(".", nodes));
            error.setMessage(messageSource.getMessage(violation.getMessage(), null, request.getLocale()));
            details.add(error);
        }
    }
}