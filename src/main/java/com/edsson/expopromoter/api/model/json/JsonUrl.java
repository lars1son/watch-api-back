package com.edsson.expopromoter.api.model.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.DEFAULT)
public class JsonUrl {
    private String url;
    private int eventId;
    public JsonUrl() {
    }

    public JsonUrl(String url,int eventId ) {
        this.url = url;
        this.eventId=eventId;
    }


    public String getUrl() {
        return this.url;
    }

    public int getEventId() {
        return eventId;
    }
}
