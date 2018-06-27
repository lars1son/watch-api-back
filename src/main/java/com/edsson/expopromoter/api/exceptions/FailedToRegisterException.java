package com.edsson.expopromoter.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static java.lang.String.format;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Failed to login")
public class FailedToRegisterException extends Exception {
    public FailedToRegisterException(String username) {
        super(format("Failed to register. Error: %s", username));
    }
}
