package com.edsson.expopromoter.api.model.json;

import com.edsson.expopromoter.api.model.TicketDAO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class JsonTicket {
    private int id;
    private String eventName;
    private String ticketImagePath;


    public JsonTicket() {
    }

    public JsonTicket(int id) {
        this.id = id;
    }

    public JsonTicket(int id, String eventName, String ticketImagePath) {
        this.id = id;
        this.eventName = eventName;
        this.ticketImagePath = ticketImagePath;
    }
    public static JsonTicket from(TicketDAO ticketDAO){
        return new JsonTicket(
                ticketDAO.getId(),
                ticketDAO.getEventsByEventId().getName(),
                ticketDAO.getImagePath()
        );
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
