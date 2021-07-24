package de.birkenfunk.birkenbotcode.persistent.entity;

import java.io.Serializable;
import java.util.Objects;

public class RoleToNameId implements Serializable {

    private Role role;
    private User user;
    private long serverId;

    public RoleToNameId() {
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleToNameId that = (RoleToNameId) o;
        return role.getRoleID()==that.role.getRoleID()
                && user.getUserID()==that.user.getUserID() && serverId==that.serverId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(role, user, serverId);
    }
}
