package com.edsson.expopromoter.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static java.lang.String.format;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="User does not exist or token expired!")
public class FailedToUpdateUserException extends Exception {
    public FailedToUpdateUserException(String username) {
        super(format("Failed to update user profile. User: %s", username));
    }
}