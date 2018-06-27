package com.edsson.expopromoter.api.model.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY)
public class GenericResponse {

    private String message;

    private String locale;

    private String timestamp;

    private String messageId;
    private String token;
    private String[] params;

    public GenericResponse(String message,  String[] params) {
        this(message,null, "default", params);
    }

    public GenericResponse( String[] params) {
        this(null,null, "default", params);
    }
    public GenericResponse(String message, String token, String locale, String[] params) {
        this.message = message;
        this.token=token;
        this.locale = locale;
        this.params = params;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        this.messageId = UUID.randomUUID().toString();
    }

    public void setToken(String token ) {
        this.token = token;
    }
}
