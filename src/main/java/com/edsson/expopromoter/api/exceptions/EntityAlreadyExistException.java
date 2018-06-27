package com.edsson.expopromoter.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.CONFLICT, reason="Entity already exist")  // 409
public class EntityAlreadyExistException extends Exception {
    public EntityAlreadyExistException( ) {
        super("Entity already exist");
    }
}
