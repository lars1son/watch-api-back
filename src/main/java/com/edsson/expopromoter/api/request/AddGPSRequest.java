package com.edsson.expopromoter.api.request;

public class AddGPSRequest {
    private float latitude;
    private String eventId;
    private float longtitude;

    public float getLatitude() {
        return latitude;
    }

    public float getLongtitude() {
        return longtitude;
    }

    public String getEventId() {
        return eventId;
    }
}
