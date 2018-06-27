package com.edsson.expopromoter.api.model;

import com.edsson.expopromoter.api.model.json.JsonTicket;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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



    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_start", columnDefinition = "DATETIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date dateStart;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_end", columnDefinition = "DATETIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date dateEnd;

    @Basic
    @Column(name = "cover_photo_path", nullable = true)
    private String photoPath;



    @Basic
    @Column(name = "info_photo_path", nullable = true)
    private String infoPhotoPath;
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

    @Basic
    @Column(name = "event_info_url", nullable = true)
    private String eventInfoUrl;

    @JsonManagedReference
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.EAGER,orphanRemoval=true)
    private List<TicketDAO> tickets;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_creator_id", referencedColumnName = "id", nullable = false)
    private User userCreatorId;


    @JsonManagedReference
    @OneToOne(mappedBy = "eventDAO", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private GpsDAO gpsDAO;



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

    public Set<JsonTicket> getTickets() {
        Set<JsonTicket> list = new HashSet<>();
        if (tickets == null) {
            return null;
        }
        for (TicketDAO ticketDAO : tickets) {
            list.add(JsonTicket.from(ticketDAO));
        }
        return list;
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

    public String getEventInfoUrl() {
        return eventInfoUrl;
    }

    public void setEventInfoUrl(String eventInfoUrl) {
        this.eventInfoUrl = eventInfoUrl;
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

    public String getInfoPhotoPath() {
        return infoPhotoPath;
    }

    public void setInfoPhotoPath(String infoPhotoPath) {
        this.infoPhotoPath = infoPhotoPath;
    }

    @Override
    public boolean equals(Object o) {
        EventDAO that = (EventDAO) o;
        if (this.userCreatorId.getId() != that.userCreatorId.getId())
            return false;
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
