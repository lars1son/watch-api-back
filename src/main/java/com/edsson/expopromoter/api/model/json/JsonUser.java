package com.edsson.expopromoter.api.model.json;

import com.edsson.expopromoter.api.context.UserContext;
import com.edsson.expopromoter.api.model.User;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class JsonUser {

    private String email;
    private long id;
    private String role;

    public JsonUser() {

    }

    public JsonUser(String email, long id, String role) {
        this.email = email;
        this.id = id;
        this.role = role;
    }

    public static JsonUser from(UserContext userContext) {
        return new JsonUser(
                userContext.getEmail(),
                userContext.getUserId(),
                userContext.getRole().getRole()
        );
    }

    public static JsonUser from(User userDAO) {
        return JsonUser.from(UserContext.create(userDAO));
    }

    public long getId() {
        return id;
    }
}