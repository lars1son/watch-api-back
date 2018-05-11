package com.edsson.expopromoter.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterDeviceRequest {
    private String deviceId;

    public RegisterDeviceRequest() {
    }

    public RegisterDeviceRequest(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }
}
