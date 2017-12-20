package com.edsson.expopromoter.api.user.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResetPasswordRequest {

    @NotNull
    private String newPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
