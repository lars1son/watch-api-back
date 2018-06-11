package com.edsson.expopromoter.api.request;

public class UpdateEmailRequest {

    String updateEmailToken;
    String newEmail;

    public String getUpdateEmailToken() {
        return updateEmailToken;
    }

    public String getNewEmail() {
        return newEmail;
    }
}
