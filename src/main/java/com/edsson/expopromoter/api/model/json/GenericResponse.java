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

    private String[] params;

    public GenericResponse(String message,  String[] params) {
        this(message, "default", params);
    }

    public GenericResponse(String message, String locale, String[] params) {
        this.message = message;
        this.locale = locale;
        this.params = params;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        this.messageId = UUID.randomUUID().toString();
    }
}
