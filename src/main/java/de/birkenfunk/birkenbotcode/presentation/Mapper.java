package de.birkenfunk.birkenbotcode.presentation;

import de.birkenfunk.birkenbotcode.domain.CommandDTO;
import de.birkenfunk.birkenbotcode.domain.RoleDTO;
import de.birkenfunk.birkenbotcode.domain.UserDTO;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.commands.Command;

import java.util.function.Function;

public class Mapper {

    public static Function<Member, UserDTO> memberToUserDTO = it -> {
        UserDTO user = new UserDTO();
        user.setName(it.getEffectiveName());
        user.setUserID(it.getIdLong());
        user.setTimeJoined(it.getTimeJoined());
        return user;
    };

    public static Function<Role, RoleDTO> roleToRoleDTO = it -> {
        RoleDTO role = new RoleDTO();
        role.setName(it.getName());
        role.setRoleID(it.getIdLong());
        return role;
    };

    public static Function<Command, CommandDTO> commandToCommandDTO = it -> {
        CommandDTO command = new CommandDTO();
        command.setDescription(it.getDescription());
        command.setName(it.getName());
        return command;
    };

}
