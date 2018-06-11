package com.edsson.expopromoter.api.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static java.lang.String.format;

@ResponseStatus(value= HttpStatus.NOT_FOUND)  // 404
public class NoSuchEventPerUserException extends Exception {
    public NoSuchEventPerUserException(String username) {
        super(format("No Such event for this user %s", username));
    }
}
