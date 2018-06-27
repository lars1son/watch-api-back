package com.edsson.expopromoter.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Entity not found or json is incorrect!")  // 409
public class EntityNotFoundException extends Exception {
    public EntityNotFoundException( ) {
        super("Entity not found or json is incorrect!");
    }
}
