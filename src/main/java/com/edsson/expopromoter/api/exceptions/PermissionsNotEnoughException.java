package com.edsson.expopromoter.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.METHOD_NOT_ALLOWED, reason="You don't have enough permission!")
public class PermissionsNotEnoughException extends Exception {
    public PermissionsNotEnoughException( ) {
        super("User doesn't have enough permission!");
    }
}
