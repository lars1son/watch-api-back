package com.edsson.expopromoter.api.model;

import com.edsson.expopromoter.api.context.UserContext;

public class Credential {
    private Long id;
    private String email;
    private String role;

    public Credential(UserContext user) {
        if (user != null) {
            this.id = user.getUserId();
            this.email = user.getEmail();
            this.role= user.getRole().getRole();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String  getRoles() {
        return role;
    }

    public void setRoles(String roles) {
        this.role  = roles ;
    }
}