package com.edsson.expopromoter.api.user.model;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "CATEGORY")
public class RoleDAO {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "roles")
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private String role;


    @OneToMany(mappedBy="owner")
    private List<User> users;


    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public RoleDAO(String role) {
        this.role = role;
    }


    public long getId() {
        return id;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
