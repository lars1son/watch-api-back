package com.edsson.expopromoter.api.model.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class JsonJpsPerEvent {


    String userEmail;
    String userName;
    String eventOpenDate;
    String phone;
    String location;

    public JsonJpsPerEvent(  String userEmail, String userName, String eventOpenDate, String phone, String location) {

        this.userEmail = userEmail;
        this.userName = userName;
        this.eventOpenDate = eventOpenDate;
        this.phone = phone;
        this.location = location;
    }
}
