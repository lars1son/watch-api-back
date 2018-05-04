package com.edsson.expopromoter.api.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "roles")
public class RoleDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "role")
    @NotNull
//    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private String role;


    @OneToMany(mappedBy="roleDAO")
    private List<User> users;

    public RoleDAO() {
    }

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
