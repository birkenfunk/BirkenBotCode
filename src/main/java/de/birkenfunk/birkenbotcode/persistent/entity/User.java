package de.birkenfunk.birkenbotcode.persistent.entity;

import javax.persistence.*;

import java.time.OffsetDateTime;

@Entity
public class User {

    private long userID;
    private String name;
    private OffsetDateTime timeJoined;

    public User() {
    }

    @Id
    @Column(name = "user_id", updatable = false, nullable = false)
    public long getUserID(){
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    @Basic
    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "time_joined", nullable = false)
    public OffsetDateTime getTimeJoined() {
        return timeJoined;
    }

    public void setTimeJoined(OffsetDateTime timeJoined) {
        this.timeJoined = timeJoined;
    }
}
