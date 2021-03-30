package de.birkenfunk.HelpClasses;

public class Command {
    private int CommandID;
    private String Description;
    private String Name;
    private boolean ServerCommand;

    public Command(int commandID, String description, String name, boolean serverCommand) {
        CommandID = commandID;
        Description = description;
        Name = name;
        ServerCommand = serverCommand;
    }

    public int getCommandID() {
        return CommandID;
    }

    public String getDescription() {
        return Description;
    }

    public String getName() {
        return Name;
    }

    public boolean isServerCommand() {
        return ServerCommand;
    }
}
