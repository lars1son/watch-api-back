package com.edsson.expopromoter.api.user.model;

import com.edsson.expopromoter.api.ticket.model.Ticket;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.edsson.expopromoter.api.core.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@SuppressWarnings("unused")
@Entity
@Table(name = "users")
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class User extends BaseModel {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String id;

    @NotNull
    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Email
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name="user_roles", joinColumns = @JoinColumn(name = "id"))
    private Set<Role> roles = new TreeSet<>();

    @Column(name = "user_type")
    @Enumerated(EnumType.ORDINAL)
    @JsonIgnore
    private UserType userType;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Ticket> tickets;

    public User() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
