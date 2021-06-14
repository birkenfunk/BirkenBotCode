package de.birkenfunk.birkenbotcode.persistent.entity;

import javax.persistence.*;

@Entity
@IdClass(ReactionRoleID.class)
public class ReactionRole {

    private String emojiID;
    private Role roleID;
    private long serverID;

    @Id
    @Column(name = "EmojiID", nullable = false)
    public String getEmojiID() {
        return emojiID;
    }

    public void setEmojiID(String emojiID) {
        this.emojiID = emojiID;
    }

    @ManyToOne
    @JoinColumn(name = "RoleID", referencedColumnName = "RoleID")
    public Role getRoleID() {
        return roleID;
    }

    public void setRoleID(Role roleID) {
        this.roleID = roleID;
    }

    @Id
    @Column(name = "ServerID", nullable = false)
    public long getServerID() {
        return serverID;
    }

    public void setServerID(long serverID) {
        this.serverID = serverID;
    }
}
