package com.change.gic.modules.file.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EntityDuplicationException extends RuntimeException {
    public EntityDuplicationException(String message) {
        super(message);
    }
}
