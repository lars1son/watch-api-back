package com.edsson.expopromoter.api.model.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.DEFAULT)
public class JsonToken {
    String token;

    public JsonToken(String token) {
        this.token = token;
    }
    public static JsonToken create(String token){
        return new JsonToken(token);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
