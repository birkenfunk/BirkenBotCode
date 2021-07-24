package de.birkenfunk.birkenbotcode.persistent.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(RoleToNameId.class)
public class RoleToName implements Serializable {

    private Role role;
    private User user;
    private long serverId;

    public RoleToName() {
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "RoleId", referencedColumnName = "RoleId")
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "UserId", referencedColumnName = "UserID")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Id
    @Column(name = "ServerId", nullable = false, updatable = false)
    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }
}
