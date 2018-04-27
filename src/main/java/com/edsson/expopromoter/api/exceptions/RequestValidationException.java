package com.edsson.expopromoter.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Validation failed")  // 422
public class RequestValidationException extends Exception {
    public RequestValidationException(BindingResult bindingResult) {
        super(bindingResult.getAllErrors().get(0).getCode());
    }
    public RequestValidationException(String message) {
        super(message);
    }
}