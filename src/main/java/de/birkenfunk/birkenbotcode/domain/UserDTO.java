package de.birkenfunk.birkenbotcode.domain;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

public class UserDTO {

    private long userID;
    private String name;
    private OffsetDateTime timeJoined;
    private Set<RoleDTO> roles;

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

    public void setRoles(Set<RoleDTO> roles) {
        this.roles = roles;
    }

    public void addRole(RoleDTO roleToAdd){
        if(roles==null)
            roles = new HashSet<>();
        roles.add(roleToAdd);
    }
}
