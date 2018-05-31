package com.edsson.expopromoter.api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "events")
public class EventDAO extends BaseModel {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @Basic
    @Column(name = "ticket_url", nullable = false)
    private String ticketUrl;

    @Basic
    @Column(name = "date_start", nullable = false)
    private Date dateStart;

    @Basic
    @Column(name = "date_end", nullable = false)
    private Date dateEnd;

    @Basic
    @Column(name = "photo_path", nullable = true)
    private String photoPath;
    @Basic
    @Column(name = "event_website", nullable = true)
    private String eventWebsite;
    @Basic
    @Column(name = "event_location", nullable = true)
    private String eventLocation;
    @Basic
    @Column(name = "description", nullable = true)
    private String description;
    @Basic
    @Column(name = "agenda", nullable = true)
    private String agenda;
    @Basic
    @Column(name = "contacts", nullable = true)
    private String contacts;

    @JsonManagedReference
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<TicketDAO> tickets;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_creator_id", referencedColumnName = "id", nullable = false)
    private User userCreatorId;


    public EventDAO() {
    }


    public User getUserCreatorId() {
        return userCreatorId;
    }

    public void setUserCreatorId(User userCreatorId) {
        this.userCreatorId = userCreatorId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getTicketUrl() {
        return ticketUrl;
    }

    public void setTicketUrl(String ticketUrl) {
        this.ticketUrl = ticketUrl;
    }

    public List<TicketDAO> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketDAO> tickets) {
        this.tickets = tickets;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }


    public String getEventWebsite() {
        return eventWebsite;
    }

    public void setEventWebsite(String eventWebsite) {
        this.eventWebsite = eventWebsite;
    }


    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getAgenda() {
        return agenda;
    }

    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }


    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public void addTicket(TicketDAO ticket) {
        if (!getTickets().contains(ticket)) {
            this.tickets.add(ticket);
        }

    }


    @Override
    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;

        EventDAO that = (EventDAO) o;
        if (!this.getName().equals(that.getName()))
            return false;
        if (this.userCreatorId.getId() != that.userCreatorId.getId())
            return false;
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

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);

        result = 31 * result + (dateStart != null ? dateStart.hashCode() : 0);
        result = 31 * result + (dateEnd != null ? dateEnd.hashCode() : 0);
        result = 31 * result + (photoPath != null ? photoPath.hashCode() : 0);
        result = 31 * result + (eventWebsite != null ? eventWebsite.hashCode() : 0);
        result = 31 * result + (eventLocation != null ? eventLocation.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (agenda != null ? agenda.hashCode() : 0);
        result = 31 * result + (contacts != null ? contacts.hashCode() : 0);

        return result;
    }


}
