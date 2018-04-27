package com.edsson.expopromoter.api.exceptions;

import static java.lang.String.format;

public class FailedToRegisterException extends Exception {
    public FailedToRegisterException(String username) {
        super(format("Failed to register. Error: %s", username));
    }
}
