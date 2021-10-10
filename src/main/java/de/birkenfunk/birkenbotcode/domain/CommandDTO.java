package de.birkenfunk.birkenbotcode.domain;

import java.util.Objects;

public class CommandDTO {

    private int id;
    private String name;
    private String description;
    private boolean serverCommand;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public boolean isServerCommand() {
		return serverCommand;
	}

	public void setServerCommand(boolean serverCommand) {
		this.serverCommand = serverCommand;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommandDTO)) return false;
        CommandDTO that = (CommandDTO) o;
        return id == that.id || name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
