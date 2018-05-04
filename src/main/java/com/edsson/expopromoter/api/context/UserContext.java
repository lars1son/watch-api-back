package com.edsson.expopromoter.api.context;

import com.edsson.expopromoter.api.model.RoleDAO;
import com.edsson.expopromoter.api.model.User;

public class UserContext {

    private Long id;
    private String email;
    private String passwordHash;
    private String phoneNumber;
    private String createdAt;
    private String updatedAt;
    private RoleDAO role;

    private long expiration;


    private UserContext() {

    }

    public static UserContext create(User userDAO) {
        UserContext userContext = new UserContext();
        userContext.id = userDAO.getId();
        userContext.email = userDAO.getEmail();
        userContext.passwordHash = userDAO.getPassword();
        userContext.role=userDAO.getRole();
        return userContext;
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

    public String getEmail() {
        return email;
    }


    public boolean haveRole(String roleName) {

        if (role.getRole().equalsIgnoreCase(roleName))
            return true;

        return false;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public long getExpiration() {
        return expiration;
    }
}
