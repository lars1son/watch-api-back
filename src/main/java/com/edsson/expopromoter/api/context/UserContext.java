package com.edsson.expopromoter.api.context;

import com.edsson.expopromoter.api.model.RoleDAO;
import com.edsson.expopromoter.api.model.User;
import com.edsson.expopromoter.api.model.json.JsonEventInfo;

import java.util.Date;
import java.util.Set;

public class UserContext {

    private Long id;
    private String email;
    private String passwordHash;
    private String phoneNumber;
    private Date createdAt;
    private Date updatedAt;
    private RoleDAO role;
    private String fullName;
    private String contactEmail;
    private long expiration;
    private Set<JsonEventInfo> listEvent;

    private UserContext() {

    }

    public static UserContext create(User userDAO) {
        UserContext userContext = new UserContext();
        userContext.id = userDAO.getId();
        userContext.email = userDAO.getEmail();
        if (userDAO.getPassword() != null) {
            userContext.passwordHash = userDAO.getPassword();
        }
        userContext.role = userDAO.getRole();
        userContext.createdAt = userDAO.getCreatedAt();
        userContext.updatedAt = userDAO.getUpdatedAt();
        userContext.phoneNumber = userDAO.getPhoneNumber();
        userContext.contactEmail = userDAO.getContactEmail();
        userContext.fullName = userDAO.getFullName();
        userContext.listEvent = userDAO.getEventDAOListID();
        return userContext;
    }

    public UserContext(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
    }

    //    public UserDAO toUserDAO(){
//        UserDAO userDAO = new UserDAO();
//        userDAO.setFirstName(firstName);
//        userDAO.setLastName(secondName);
//        userDAO.setEmail(email);
//        userDAO.setLogin(login);
//        userDAO.setPasswordHash(passwordHash);
//        userDAO.setUserId(userId);
//        userDAO.setRoles(authorities);
//        userDAO.setLocale(locale);
//        userDAO.setLocked(isUserLocked);
//        return userDAO;
//    }


    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Long getUserId() {
        return id;
    }

    public RoleDAO getRole() {
        return role;
    }

    public String getPasswordHash() {
        return passwordHash;
    }


//    public Set<GrantedAuthority> getAuthorities() {
//        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
//        for (RoleDAO role : authorities){
//            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
//        }
//        return grantedAuthorities;
//    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }


    public boolean haveRole(String roleName) {

        if (role.getRole().equalsIgnoreCase(roleName))
            return true;

        return false;
    }


    public Set<JsonEventInfo> getListEvent() {
        return listEvent;
    }

    public void setListEvent(Set<JsonEventInfo> listEvent) {
        this.listEvent = listEvent;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public long getExpiration() {
        return expiration;
    }
}
