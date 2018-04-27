package com.edsson.expopromoter.api.user.model;

import java.util.Set;
import java.util.TreeSet;

public class Credential {
    private String id;
    private String email;
    private RoleDAO roles;

    public Credential(User user) {
        if (user != null) {
            this.id = user.getId();
            this.email = user.getEmail();
            this.roles = user.getRole();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RoleDAO getRoles() {
        return roles;
    }

    public void setRoles(RoleDAO roles) {
        this.roles = roles;
    }
}