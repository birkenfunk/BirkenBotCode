package de.birkenfunk.birkenbotcode.persistent.entity;

import javax.persistence.*;

@Entity
public class Command {
    private int id;
    private String name;
    private String description;
    private boolean serverCommand;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "command_id", updatable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "description", nullable = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    @Basic
    @Column(name = "server", nullable = false)
    public boolean isServerCommand() {
    	return serverCommand;
    }
    
    public void setServerCommand(boolean isServerCommand) {
		serverCommand = isServerCommand;
	}
}
