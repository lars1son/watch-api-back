package com.edsson.expopromoter.api.ticket.model;

import com.edsson.expopromoter.api.core.model.BaseModel;
import com.edsson.expopromoter.api.user.model.Event;
import com.edsson.expopromoter.api.user.model.User;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
@Entity
@Table(name = "tickets")
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
public class Ticket extends BaseModel {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String id;

    @OneToOne
    @NotNull
    private User user;

    @OneToOne
    @NotNull
    private Event event;

    public Ticket() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
