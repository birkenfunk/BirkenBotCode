package de.birkenfunk.birkenbotcode.domain;

import java.sql.Timestamp;

public class LogDTO {

    private int id;
    private CommandDTO command;
    private Timestamp time;
    private UserDTO user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CommandDTO getCommand() {
        return command;
    }

    public void setCommand(CommandDTO command) {
        this.command = command;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
