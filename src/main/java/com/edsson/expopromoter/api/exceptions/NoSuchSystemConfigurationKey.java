package com.edsson.expopromoter.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static java.lang.String.format;
@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason = "No such system configuration key!")
public class NoSuchSystemConfigurationKey extends SystemConfigurationException {
    public NoSuchSystemConfigurationKey(String key) {
        super(format("No such system configuration key: %s", key));
    }
}
