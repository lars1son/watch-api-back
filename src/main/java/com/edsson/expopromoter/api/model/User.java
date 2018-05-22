package com.edsson.expopromoter.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
@Entity
@Table(name = "users")
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
    @Column(name = "full_name")
    private String fullname;
    @Column(name = "contact_email")
    private String contactEmail;
//
//    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
//    @CollectionTable(name="user_roles", joinColumns = @JoinColumn(name = "id"))
//    private Set<Role> roles = new TreeSet<>();

//    @Column(name = "role")
//    @Enumerated(EnumType.ORDINAL)
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "id",insertable = false,updatable = false)
    @JoinColumn(name = "role", referencedColumnName = "id", nullable = false)
    private RoleDAO roleDAO;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<TicketDAO> tickets;

    @JsonManagedReference
    @OneToMany(mappedBy = "userCreatorId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<EventDAO> eventDAOList;



    public User() {}


    public void setId(Long id) {
        this.id = id;
    }

    public RoleDAO getRoleDAO() {
        return roleDAO;
    }

    public void setRoleDAO(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    public Set<EventDAO> getEventDAOList() {
        return eventDAOList;
    }

    public void addToTicketDAOList(TicketDAO ticket){
        this.tickets.add(ticket);
    }
//    public void setEventDAOList(List<EventDAO> eventDAOList) {
//        this.eventDAOList = eventDAOList;
//    }
    public void addToEventDAOList(EventDAO eventDAO){
        this.eventDAOList.add(eventDAO);
    }
    public void deleteRecordFromEventDAOList(EventDAO eventDAO){
        this.eventDAOList.remove(eventDAO);
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

    public String getFullName() {
        return fullname;
    }

    public void setFullName(String fullname) {
        this.fullname = fullname;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
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

    public Set<TicketDAO> getTickets() {
        return tickets;
    }

    public void setTickets(Set<TicketDAO> tickets) {
        this.tickets = tickets;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
