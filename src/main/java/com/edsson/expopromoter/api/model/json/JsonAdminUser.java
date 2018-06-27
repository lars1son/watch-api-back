package com.edsson.expopromoter.api.model.json;


import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class JsonAdminUser {
    String email;
    String token;

    public JsonAdminUser() {

    }

    public JsonAdminUser(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public static JsonAdminUser from(String email, String token) {
        return new JsonAdminUser(
                email, token
        );
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}
