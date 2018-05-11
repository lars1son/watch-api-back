package com.edsson.expopromoter.api.model.json;

import com.edsson.expopromoter.api.model.EventDAO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.Date;


@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class JsonEventInfo {

    private int id;
    private String name;
    private String ticketUrl;
    private Date dateStart;
    private Date dateEnd;
    private String photo;
    private String eventWebsite;
    private String eventLocation;
    private String description;
    private String agenda;
    private String contacts;
    private Long userCreatorId;

    public JsonEventInfo() {
    }

    public JsonEventInfo(int id, String name, String ticketUrl, Date dateStart, Date dateEnd, String photoPath, String eventWebsite, String eventLocation, String description, String agenda, String contacts,   Long userCreatorId) {
        this.id = id;
        this.name = name;
        this.ticketUrl = ticketUrl;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.photo = photoPath;
        this.eventWebsite = eventWebsite;
        this.eventLocation = eventLocation;
        this.description = description;
        this.agenda = agenda;
        this.contacts = contacts;
        this.userCreatorId = userCreatorId;
    }

    public static JsonEventInfo from(EventDAO eventDAO) {
        return new JsonEventInfo(
               eventDAO.getId(),
               eventDAO.getName(),
               eventDAO.getTicketUrl(),
               eventDAO.getDateStart(),
               eventDAO.getDateEnd(),
               eventDAO.getPhotoPath(),
               eventDAO.getEventWebsite(),
               eventDAO.getEventLocation(),
               eventDAO.getDescription(),
               eventDAO.getAgenda(),
               eventDAO.getContacts(),
               eventDAO.getUserCreatorId().getId()
        );
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
