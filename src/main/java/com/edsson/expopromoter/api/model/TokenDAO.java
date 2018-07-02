package com.edsson.expopromoter.api.model;

import javax.persistence.*;

@Entity
@Table(name = "token")
public class TokenDAO extends BaseModel {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "token", nullable = false)
    private String token;


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    public TokenDAO( ) {

    }


    public User getUser() {
        return user;
    }

    public TokenDAO(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
