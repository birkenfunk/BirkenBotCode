package de.birkenfunk.birkenbotcode.domain;

import de.birkenfunk.birkenbotcode.persistent.entity.Role;

public class ReactionRoleDTO {

    private String emojiID;
    private RoleDTO roleID;
    private long serverID;

    public String getEmojiID() {
        return emojiID;
    }

    public void setEmojiID(String emojiID) {
        this.emojiID = emojiID;
    }

    public RoleDTO getRoleID() {
        return roleID;
    }

    public void setRoleID(RoleDTO roleID) {
        this.roleID = roleID;
    }

    public long getServerID() {
        return serverID;
    }

    public void setServerID(long serverID) {
        this.serverID = serverID;
    }
}
