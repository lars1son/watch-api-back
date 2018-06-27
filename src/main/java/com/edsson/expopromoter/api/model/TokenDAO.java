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


    public TokenDAO( ) {

    }

    public TokenDAO(String token) {
        this.token = token;
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
