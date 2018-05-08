package com.edsson.expopromoter.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static java.lang.String.format;

@ResponseStatus(value= HttpStatus.UNAUTHORIZED, reason="Failed to login")  // 401
public class FailedToLoginException extends Exception {
    public FailedToLoginException(String username) {
        super(format("Failed to login with username %s", username));
    }
}
