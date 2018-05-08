package com.edsson.expopromoter.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationRequest {

    private String password;
    private String email;

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }


    public RegistrationRequest() {
    }

    public RegistrationRequest(String email, String password) {
        this.password = password;
        this.email = email;
    }
}
