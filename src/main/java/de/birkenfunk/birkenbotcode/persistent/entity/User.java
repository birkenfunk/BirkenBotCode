package de.birkenfunk.birkenbotcode.persistent.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.OffsetDateTime;

@Entity
public class User {

    private long userID;
    private String name;
    private OffsetDateTime timeJoined;

    public User() {
    }

    @Id
    @Column(name = "UserID", updatable = false, nullable = false)
    public long getUserID(){
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    @Basic
    @Column(name = "Name", nullable = false)
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "TimeJoined", nullable = false)
    public OffsetDateTime getTimeJoined() {
        return timeJoined;
    }

    public void setTimeJoined(OffsetDateTime timeJoined) {
        this.timeJoined = timeJoined;
    }
}
