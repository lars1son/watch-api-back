package com.edsson.expopromoter.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static java.lang.String.format;

@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Image was not loaded to storage!")
public class FailedToUploadImageToAWSException extends Exception {
    public FailedToUploadImageToAWSException( ) {
        super("Image was not loaded to AWS");
    }
}