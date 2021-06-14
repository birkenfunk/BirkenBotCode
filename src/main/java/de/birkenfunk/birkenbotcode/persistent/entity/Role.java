package de.birkenfunk.birkenbotcode.persistent.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Role {

    private int roleID;
    private String name;

    @Id
    @Column(name = "RoleID", updatable = false, nullable = false)
    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
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
