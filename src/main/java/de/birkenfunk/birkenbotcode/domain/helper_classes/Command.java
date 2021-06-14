package de.birkenfunk.birkenbotcode.domain.helper_classes;

public class Command {
    private int commandID;
    private String description;
    private String name;
    private boolean serverCommand;

    public Command(int commandID, String description, String name, boolean serverCommand) {
        this.commandID = commandID;
        this.description = description;
        this.name = name;
        this.serverCommand = serverCommand;
    }

    public int getCommandID() {
        return commandID;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public boolean isServerCommand() {
        return serverCommand;
    }
}
