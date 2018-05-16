package com.edsson.expopromoter.api.model.json;

import com.edsson.expopromoter.api.context.UserContext;
import com.edsson.expopromoter.api.model.User;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class JsonUser {

    private String email;
    private long id;
    private String role;

    private String fullName;
    private String phoneNumber;
    private String contactEmail;
    public JsonUser() {

    }

    public JsonUser(String email, long id, String role, String fullName,String phoneNumber, String contactEmail) {
        this.email = email;
        this.id = id;
        this.role = role;
        this.fullName = fullName;
        this.phoneNumber=phoneNumber;
        this.contactEmail=contactEmail;
    }

//    public JsonUser(String email, long id, String role, String password, String fullName) {
//        this.email = email;
//        this.id = id;
//        this.role = role;
//        this.password = password;
//        this.fullName = fullName;
//    }

    public static JsonUser from(UserContext userContext) {
        return new JsonUser(
                userContext.getEmail(),
                userContext.getUserId(),
                userContext.getRole().getRole(),
                userContext.getFullName(),
                userContext.getPhoneNumber(),
                userContext.getContactEmail()

        );
    }
//    public static JsonUser fullInfoFrom(UserContext userContext) {
//        return new JsonUser(
//                userContext.getEmail(),
//                userContext.getUserId(),
//                userContext.getRole().getRole()
//        );
//    }




    public static JsonUser from(User userDAO) {
        return JsonUser.from(UserContext.create(userDAO));
    }

    public long getId() {
        return id;
    }
}