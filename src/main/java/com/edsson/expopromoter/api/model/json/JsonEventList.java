package com.edsson.expopromoter.api.model.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class JsonEventList {
    List<JsonEventInfo> list;

    public JsonEventList() {
    }

    public JsonEventList(List<JsonEventInfo> list) {
        this.list = list;
    }

    public void setList(List<JsonEventInfo> list) {
        this.list = list;
    }

}
