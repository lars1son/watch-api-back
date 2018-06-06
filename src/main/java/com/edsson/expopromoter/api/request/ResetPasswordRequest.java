package com.edsson.expopromoter.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;

public class ResetPasswordRequest {


    String email;
    String client;
//    public String getEmail() {
//        return email;
//    }

    public String getEmail() {
        return email;
    }

    public String getClient() {
        return client;
    }
}