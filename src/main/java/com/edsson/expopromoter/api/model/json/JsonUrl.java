package com.edsson.expopromoter.api.model.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.DEFAULT)
public class JsonUrl {
    private String url;

    public JsonUrl() {
    }

    public JsonUrl(String url) {
        this.url = url;
    }


    public String getUrl() {
        return this.url;
    }
}
