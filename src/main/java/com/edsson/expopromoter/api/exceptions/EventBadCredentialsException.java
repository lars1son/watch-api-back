package com.edsson.expopromoter.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Required fields are empty!")
public class EventBadCredentialsException extends Exception {

    public EventBadCredentialsException( ) {
        super("Required fields are empty!");
    }
}
