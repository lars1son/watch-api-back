package com.edsson.expopromoter.api.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static java.lang.String.format;

@ResponseStatus(value= HttpStatus.NOT_FOUND)  // 404
public class NoSuchUserException extends Exception {
    public NoSuchUserException(String username) {
        super(format("No Such user exception %s", username));
    }
}
