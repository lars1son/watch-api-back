package com.edsson.expopromoter.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateEventRequest {



    private int id;
    private String coverImageBase64;
    private String infoImageBase64;
    private String dateStart;
    private String name;
    private String dateEnd;
    private String location;
    private String website;

    private String description;
    private String agenda;
    private String contacts;
    private String ticketUrl;


    public CreateEventRequest() {
    }

    public CreateEventRequest(String dateStart, String name, String dateEnd, String location, String website, String description, String agenda, String contacts, String ticketUrl) {
        this.dateStart = dateStart;
        this.name = name;
        this.dateEnd = dateEnd;
        this.location = location;
        this.website = website;
        this.description = description;
        this.agenda = agenda;
        this.contacts = contacts;
        this.ticketUrl = ticketUrl;
    }

    public String getTicketUrl() {
        return ticketUrl;
    }

    public void setTicketUrl(String ticketUrl) {
        this.ticketUrl = ticketUrl;
    }

    public String getCoverImageBase64() {
        return coverImageBase64;
    }

    public void setCoverImageBase64(String coverImageBase64) {
        this.coverImageBase64 = coverImageBase64;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;


    }

    public String getInfoImageBase64() {
        return infoImageBase64;
    }

    public void setInfoImageBase64(String infoImageBase64) {
        this.infoImageBase64 = infoImageBase64;
    }

    //    public int getEventId() {
//        return eventId;
//    }
//
//    public void setEventId(int eventId) {
//        this.eventId = eventId;
//    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAgenda() {
        return agenda;
    }

    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
