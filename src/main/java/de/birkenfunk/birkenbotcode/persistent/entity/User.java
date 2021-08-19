package de.birkenfunk.birkenbotcode.persistent.entity;

import javax.persistence.*;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User {

    private long userID;
    private String name;
    private OffsetDateTime timeJoined;
    private Set<Role> roles = new HashSet<>();

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

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "user_roles",
            joinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "user_id",
            nullable = false, updatable = false)},
            inverseJoinColumns = {
            @JoinColumn(name = "role_id",referencedColumnName = "role_id"
            ,nullable = false, updatable = false)}
    )
    public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
    
    public void addRoleToUser(Role roleToAdd){
        roles.add(roleToAdd);
    }

    public void removeRoleFromUser(Role roleToRemove){
        roles.remove(roleToRemove);
    }
}
