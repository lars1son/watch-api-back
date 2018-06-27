package com.edsson.expopromoter.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static java.lang.String.format;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason = "No such ticket for this user!")
public class NoSuchTicketPerUserException extends Exception {
    public NoSuchTicketPerUserException(String key){
        super(format("No such system configuration key: %s", key));
    }
}
