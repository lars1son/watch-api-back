package com.edsson.expopromoter.api.request;

import com.edsson.expopromoter.api.model.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;
import java.util.Set;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateUserRequest {

    @NotNull
    private String email;

    @NotNull
    private String password;

    private Set<Role> roles;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
