package com.edsson.expopromoter.api.model.json;

import com.edsson.expopromoter.api.model.EventDAO;
import com.edsson.expopromoter.api.model.TicketDAO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;


@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class JsonEventInfo {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private int id;
    private String name;
    private String ticketUrl;
    private String dateStart;
    private String dateEnd;
    private String coverImageBase64;
    private String infoImageBase64;
    private String website;
    private String eventLocation;
    private String description;
    private String agenda;
    private String contacts;
    private String eventInfoUrl;
    private Long userCreatorId;
    private Set<JsonTicket>  ticketDAOS;
    public JsonEventInfo() {
    }

    public JsonEventInfo(int id, String name, String ticketUrl, Date dateStart, Date dateEnd,  String eventWebsite, String eventLocation, String description, String agenda, String contacts,String coverImageBase64, String infoImageBase64 ,  Long userCreatorId,  String eventInfoUrl, Set<JsonTicket> tickets) {
        this.id = id;
        this.name = name;
        this.ticketUrl = ticketUrl;
        this.dateStart = formatter.format(dateStart);
        this.dateEnd = formatter.format(dateEnd);
        this.coverImageBase64 = coverImageBase64;
        this.infoImageBase64=infoImageBase64;
        this.website = eventWebsite;
        this.eventLocation = eventLocation;
        this.description = description;
        this.agenda = agenda;
        this.contacts = contacts;
        this.userCreatorId = userCreatorId;
        this.eventInfoUrl=eventInfoUrl;
        this.ticketDAOS=tickets;
    }

    public static JsonEventInfo from(EventDAO eventDAO) {

        return new JsonEventInfo(
               eventDAO.getId(),
               eventDAO.getName(),
               eventDAO.getTicketUrl(),
               eventDAO.getDateStart(),
               eventDAO.getDateEnd(),
               eventDAO.getEventWebsite(),
               eventDAO.getEventLocation(),
               eventDAO.getDescription(),
               eventDAO.getAgenda(),
               eventDAO.getContacts(),
               eventDAO.getPhotoPath(),
               eventDAO.getInfoPhotoPath(),
               eventDAO.getUserCreatorId().getId(),
               eventDAO.getEventInfoUrl(),
               eventDAO.getTickets()
        );
    }

    public void setPhoto(String photo) {
        this.coverImageBase64 = photo;
    }
}
