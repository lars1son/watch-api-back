package com.edsson.expopromoter.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Failed to create reset password token")  // 500
public class FailedToCreateResetPasswordTokenException extends Exception {
    public FailedToCreateResetPasswordTokenException() {
        super("Failed to create reset password token ");
    }
}
