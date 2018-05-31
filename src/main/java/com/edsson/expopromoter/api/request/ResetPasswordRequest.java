package com.edsson.expopromoter.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;

public class ResetPasswordRequest {


    String email;

    public String getEmail() {
        return email;
    }

}