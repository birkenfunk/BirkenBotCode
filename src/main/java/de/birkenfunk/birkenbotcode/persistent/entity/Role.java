package de.birkenfunk.birkenbotcode.persistent.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Role {

    private long roleID;
    private String name;

    @Id
    @Column(name = "RoleID", updatable = false, nullable = false)
    public long getRoleID() {
        return roleID;
    }

    public void setRoleID(long roleID) {
        this.roleID = roleID;
    }

    @Basic
    @Column(name = "RoleName", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
