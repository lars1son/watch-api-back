package com.edsson.expopromoter.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNAUTHORIZED, reason="Password is incorrect!")  // 401
public class PasswordIncorrectException extends Exception {
    public PasswordIncorrectException(){
        super();
    }
}
