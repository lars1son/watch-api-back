package com.edsson.expopromoter.api.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason = "Role 'Administrator' required!")  // 404
public class PrivilegiousException extends Exception {
    public PrivilegiousException( ) {
        super("Role: ADMINISTRATOR required!");
    }
}