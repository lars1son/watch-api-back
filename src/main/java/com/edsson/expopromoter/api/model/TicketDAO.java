package com.edsson.expopromoter.api.model;

import javax.persistence.*;

@Entity
@Table(name = "tickets")
public class TicketDAO extends BaseModel {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;


    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id", nullable = false)
    private EventDAO event;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = true)
    private User user;
    @Column(name = "image_path")
    private String imagePath;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }


    public EventDAO getEventsByEventId() {
        return event;
    }

    public void setEventsByEventId(EventDAO eventsByEventId) {
        this.event = eventsByEventId;
    }

    @Override
    public boolean equals(Object o) {

        TicketDAO that = (TicketDAO) o;
        if (this.getId() != that.getId()) return false;

        return true;
    }
}
