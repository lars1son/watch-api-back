package com.edsson.expopromoter.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@SuppressWarnings("unused")
@Entity
@Table(name = "user")
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class User extends BaseModel {
//public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @JsonIgnore
    @Column(name = "password")
    private String password;

    @NotNull
    @Email
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;
//
//    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
//    @CollectionTable(name="user_roles", joinColumns = @JoinColumn(name = "id"))
//    private Set<Role> roles = new TreeSet<>();

//    @Column(name = "role")
//    @Enumerated(EnumType.ORDINAL)
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "id",insertable = false,updatable = false)
    @JoinColumn(name = "role", referencedColumnName = "id", nullable = false)
    private RoleDAO roleDAO;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Ticket> tickets;

    public User() {}

    public User(String password, String email) {
        this.password = password;
        this.email = email;
    }

    public User(String email, String password, RoleDAO roleUser) {
        this.password=password;
        this.email=email;
        this.roleDAO=roleUser;
    }

    public Long getId() {
        return id;
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

    public RoleDAO getRole() {
        return roleDAO;
    }

    public void setRole(RoleDAO userType) {
        this.roleDAO = userType;
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
