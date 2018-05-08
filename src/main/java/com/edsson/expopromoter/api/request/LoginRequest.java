package com.edsson.expopromoter.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequest {
    private String email;

    private String password;

    public String getEmail() {
        return email;
    }


    public String getPassword() {
        return password;
    }


}