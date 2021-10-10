package de.birkenfunk.birkenbotcode.persistent.entity;

import net.dv8tion.jda.api.interactions.commands.OptionType;

import javax.persistence.*;

@Entity
@Table(name = "option_data")
public class OptionData {
    private int id;
    private String name;
    private OptionType optionType;
    private boolean optional;
    private String description;
    private Command command;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_data_id", updatable = false)
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
    @Column(name = "option_type", nullable = false)
    public OptionType getOptionType() {
        return optionType;
    }

    public void setOptionType(OptionType optionType) {
        this.optionType = optionType;
    }
    
    @Basic
    @Column(name = "server", nullable = false)
    public boolean isOptional() {
    	return optional;
    }
    
    public void setOptional(boolean isOptional) {
		optional = isOptional;
	}

    @ManyToOne
    public Command getCommand(){
        return command;
    }

    public void setCommand(Command command){
        this.command = command;
    }
}
