package de.birkenfunk.birkenbotcode.domain;

import de.birkenfunk.birkenbotcode.persistent.entity.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import javax.persistence.*;

public class OptionDataDTO {
    private int id;
    private String name;
    private OptionType optionType;
    private boolean optional;
    private String description;
    private CommandDTO command;

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

    public OptionType getOptionType() {
        return optionType;
    }

    public void setOptionType(OptionType optionType) {
        this.optionType = optionType;
    }

    public boolean isOptional() {
    	return optional;
    }
    
    public void setOptional(boolean isOptional) {
		optional = isOptional;
	}

    public CommandDTO getCommand(){
        return command;
    }

    public void setCommand(CommandDTO command){
        this.command = command;
    }
}
