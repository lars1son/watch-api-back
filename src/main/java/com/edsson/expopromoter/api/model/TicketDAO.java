package com.edsson.expopromoter.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "tickets")
public class TicketDAO  extends BaseModel{
    @Id
    @Column(name = "id", nullable = false)
    private String id;



    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id", nullable = false)
    private EventDAO eventsByEventId;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id", nullable=true)
    private User user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public EventDAO getEventsByEventId() {
        return eventsByEventId;
    }

    public void setEventsByEventId(EventDAO eventsByEventId) {
        this.eventsByEventId = eventsByEventId;
    }
}
