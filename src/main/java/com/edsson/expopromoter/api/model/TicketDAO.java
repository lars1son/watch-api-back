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
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;

        TicketDAO that = (TicketDAO) o;
        if (this.getId() != that.getId()) return false;
        if (this.getEventsByEventId().getId() != that.getEventsByEventId().getId()) return false;
        if (this.getUser().getId() != that.getUser().getId()) return false;
//        if (id != that.id) return false;
//        if (name != null ? !name.equals(that.name) : that.name != null) return false;
//
//        if (dateStart != null ? !dateStart.equals(that.dateStart) : that.dateStart != null) return false;
//        if (dateEnd != null ? !dateEnd.equals(that.dateEnd) : that.dateEnd != null) return false;
//        if (photoPath != null ? !photoPath.equals(that.photoPath) : that.photoPath != null) return false;
//        if (eventWebsite != null ? !eventWebsite.equals(that.eventWebsite) : that.eventWebsite != null) return false;
//        if (eventLocation != null ? !eventLocation.equals(that.eventLocation) : that.eventLocation != null)
//            return false;
//        if (description != null ? !description.equals(that.description) : that.description != null) return false;
//        if (agenda != null ? !agenda.equals(that.agenda) : that.agenda != null) return false;
//        if (contacts != null ? !contacts.equals(that.contacts) : that.contacts != null) return false;


        return true;
    }
}
