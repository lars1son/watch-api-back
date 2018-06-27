package com.edsson.expopromoter.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Token for you not exist exception.")
public class TokenNotExistException extends Exception {
    public TokenNotExistException(){
        super("Token for you not exist exception.");
    }
}
