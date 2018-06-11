package com.edsson.expopromoter.api.exceptions;

import static java.lang.String.format;

public class FailedToUpdateUserException extends Exception {
    public FailedToUpdateUserException(String username) {
        super(format("Failed to update user profile. User: %s", username));
    }
}