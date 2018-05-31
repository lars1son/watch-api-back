package com.edsson.expopromoter.api.request;

public class UpdatePasswordRequest {

    String updatePasswordToken;
    String newPassword;

    public String getUpdatePasswordToken() {
        return updatePasswordToken;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
