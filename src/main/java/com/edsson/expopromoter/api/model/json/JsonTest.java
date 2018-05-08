package com.edsson.expopromoter.api.model.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class JsonTest {
    private String line;

    public JsonTest() {
    }

    public JsonTest(String line) {
        this.line = line;
    }


    public String getLine() {
        return line;
    }
}
