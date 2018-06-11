package com.edsson.expopromoter.api.model;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity
@Table(name = "PASSWORD_RESET_TOKEN")
public class PasswordResetTokenDAO {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(name = "expiredate")
    private Date expireDate;

    public PasswordResetTokenDAO() {

    }

    /**
     * @param userDAO    user to create token for
     * @param token      token string
     * @param expiration hours
     */
    public PasswordResetTokenDAO(User userDAO, String token, int expiration) {
        this.user = userDAO;
        this.token = token;

        LocalDateTime now = LocalDateTime.now().plusHours(expiration);
        Instant instant = now.atZone(ZoneId.systemDefault()).toInstant();
        Date date= Date.from(instant);

        this.expireDate = date;
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public Date getExpiryDate() {
        return expireDate;
    }

    public void setExpiryDate(Date expiration) {
        this.expireDate = expiration;
    }
}