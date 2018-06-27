package com.edsson.expopromoter.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Internal server error. System configuration exception.")
public class SystemConfigurationException extends Exception {
    public SystemConfigurationException(String message) {
        super(message);
    }
}
