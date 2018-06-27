package com.edsson.expopromoter.api.request;

public class AddTicketRequest {
    private String imageBase64;
    private Integer eventId;

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public int getEventId() {

        if (eventId==null){
            return 0;
        }
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
}
