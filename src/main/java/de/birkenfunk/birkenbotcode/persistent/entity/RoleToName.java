package de.birkenfunk.birkenbotcode.persistent.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "role_to_name")
@IdClass(RoleToNameId.class)
public class RoleToName implements Serializable {

    private Role role;
    private User user;
    private long serverId;

    public RoleToName() {
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Id
    @Column(name = "server_id", nullable = false, updatable = false)
    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }
}
