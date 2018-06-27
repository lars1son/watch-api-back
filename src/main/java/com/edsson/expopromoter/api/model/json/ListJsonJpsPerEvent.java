package com.edsson.expopromoter.api.model.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ListJsonJpsPerEvent {
    String eventName;
    String eventDate;

    List<JsonJpsPerEvent> list ;

    public ListJsonJpsPerEvent(String eventName, String eventDate, List<JsonJpsPerEvent> list) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.list = list;
    }
}
