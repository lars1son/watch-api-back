package com.edsson.expopromoter.api.model;

import com.edsson.expopromoter.api.config.SystemConfigurationKeys;
import com.edsson.expopromoter.api.exceptions.NoSuchSystemConfigurationKey;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "SYSTEM_CONFIGURATION")
public class SystemConfigurationDAO {

    @Id
    @Column(name = "CONFIG_KEY", unique = true, nullable = false)
    private String key;

    @Column(name = "CONFIG_VALUE", columnDefinition = "TEXT")
    private String value;

    @Column(name = "CREATED")
    private Date created;

    @Column(name = "UPDATED")
    private Date updated;

    public String getKey() {
        return key;
    }

    public void setKey(String key) throws NoSuchSystemConfigurationKey {

        if(!SystemConfigurationKeys.isSystemConfigurationKeyExist(key)){
            throw new NoSuchSystemConfigurationKey(key);
        }

        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @PrePersist
    protected void onCreate() {
        created = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updated = new Date();
    }
}
