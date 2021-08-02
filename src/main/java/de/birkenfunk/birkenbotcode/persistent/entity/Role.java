package de.birkenfunk.birkenbotcode.persistent.entity;

import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Role {

    private long roleID;
    private String name;

    @Id
    @Column(name = "role_id", updatable = false, nullable = false)
    public long getRoleID() {
        return roleID;
    }

    public void setRoleID(long roleID) {
        this.roleID = roleID;
    }

    @Basic
    @Column(name = "role_name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
