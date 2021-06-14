package de.birkenfunk.birkenbotcode.domain;

import java.time.OffsetDateTime;

public class UserDTO {

    private long userID;
    private String name;
    private OffsetDateTime timeJoined;

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OffsetDateTime getTimeJoined() {
        return timeJoined;
    }

    public void setTimeJoined(OffsetDateTime timeJoined) {
        this.timeJoined = timeJoined;
    }
}
