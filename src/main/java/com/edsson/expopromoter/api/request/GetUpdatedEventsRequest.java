package com.edsson.expopromoter.api.request;

import javax.validation.constraints.NotNull;

public class GetUpdatedEventsRequest {
    @NotNull
    private String lastUpdate;

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
