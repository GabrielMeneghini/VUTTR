package com.apiRest.VUTTR.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoUpdateDetectedException extends RuntimeException {
    public NoUpdateDetectedException(String message) {
        super(message);
    }
}
