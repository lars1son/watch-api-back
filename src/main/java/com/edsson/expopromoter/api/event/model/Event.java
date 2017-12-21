package com.edsson.expopromoter.api.event.model;

import com.edsson.expopromoter.api.core.model.BaseModel;
import com.edsson.expopromoter.api.ticket.model.Ticket;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@SuppressWarnings("unused")
@Entity
@Table(name = "events")
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
public class Event extends BaseModel {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String id;

    @OneToMany(targetEntity = Ticket.class, mappedBy = "event", cascade = CascadeType.ALL)
    private List<Ticket> tickets;

    public Event() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}
