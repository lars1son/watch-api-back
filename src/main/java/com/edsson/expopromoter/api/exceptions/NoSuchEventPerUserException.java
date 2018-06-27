package com.edsson.expopromoter.api.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static java.lang.String.format;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason = "This user does not have such event!")  // 404
public class NoSuchEventPerUserException extends Exception {
    public NoSuchEventPerUserException( ) {
        super(format("No Such event for this user"));
    }
}
