package com.edsson.expopromoter.api.model.json;


import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class JsonAdminUser {
    String email;
    String token;
    long count;

    public JsonAdminUser() {

    }

    public JsonAdminUser(String email, String token, long count) {
        this.email = email;
        this.token = token;
        this.count= count;
    }

    public static JsonAdminUser from(String email, String token, double count) {
        return new JsonAdminUser(
                email, token,(long) count
        );
    }

    public long getCount() {
        return count;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}
