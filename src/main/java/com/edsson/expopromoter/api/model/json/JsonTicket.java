package com.edsson.expopromoter.api.model.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class JsonTicket {
    private int id;
    private String eventName;
    private String ticketImageBase64;


    public JsonTicket() {
    }

    public JsonTicket(int id) {
        this.id = id;
    }

    public JsonTicket(int id, String eventName, String ticketImageBase64) {
        this.id = id;
        this.eventName = eventName;
        this.ticketImageBase64 = ticketImageBase64;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
