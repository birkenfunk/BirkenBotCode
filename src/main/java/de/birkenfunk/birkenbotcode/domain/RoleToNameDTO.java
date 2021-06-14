package de.birkenfunk.birkenbotcode.domain;

import de.birkenfunk.birkenbotcode.persistent.entity.Role;
import de.birkenfunk.birkenbotcode.persistent.entity.User;

public class RoleToNameDTO {

    private RoleDTO role;
    private UserDTO user;
    private long serverId;

    public RoleDTO getRole() {
        return role;
    }

    public void setRole(RoleDTO role) {
        this.role = role;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }
}
