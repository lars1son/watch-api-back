package com.edsson.expopromoter.api.model.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.DEFAULT)
public class JsonUrl {
    private String coverPhotoPath;
    private String infoPhotoPath;
    private int eventId;
    private String eventInfoUrl;
    public JsonUrl() {
    }

    public JsonUrl(String coverPhotoPath,String infoPhotoPath,int eventId, String eventInfoUrl ) {
        this.coverPhotoPath = coverPhotoPath;
        this.eventId=eventId;
        this.eventInfoUrl=eventInfoUrl;
        this.infoPhotoPath=infoPhotoPath;
    }

    public String getCoverPhotoPath() {
        return coverPhotoPath;
    }

    public String getInfoPhotoPath() {
        return infoPhotoPath;
    }

    public int getEventId() {
        return eventId;
    }

    public String getEventInfoUrl() {
        return eventInfoUrl;
    }
}
