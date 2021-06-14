package de.birkenfunk.birkenbotcode.persistent.entity;

import java.io.Serializable;
import java.util.Objects;

public class ReactionRoleID implements Serializable {

    private String emojiID;
    private long serverID;

    public String getEmojiID() {
        return emojiID;
    }

    public void setEmojiID(String emojiID) {
        this.emojiID = emojiID;
    }

    public long getServerID() {
        return serverID;
    }

    public void setServerID(long serverID) {
        this.serverID = serverID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReactionRoleID that = (ReactionRoleID) o;
        return serverID == that.serverID && Objects.equals(emojiID, that.emojiID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emojiID, serverID);
    }
}
