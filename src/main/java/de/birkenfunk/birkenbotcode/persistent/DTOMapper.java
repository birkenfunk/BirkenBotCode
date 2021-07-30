package de.birkenfunk.birkenbotcode.persistent;

import de.birkenfunk.birkenbotcode.domain.*;
import de.birkenfunk.birkenbotcode.persistent.entity.*;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class DTOMapper {

    public Function<Command, CommandDTO> commandToCommandDTOFunction = command -> {
        CommandDTO commandDTO = new CommandDTO();
        commandDTO.setDescription(command.getDescription());
        commandDTO.setId(command.getId());
        commandDTO.setName(command.getName());
        commandDTO.setServerCommand(command.isServerCommand());
        return commandDTO;
    };

    public Function<CommandDTO, Command> commandDTOToCommandFunction = commandDTO -> {
        Command command = new Command();
        command.setDescription(command.getDescription());
        command.setId(command.getId());
        command.setName(command.getName());
        command.setServerCommand(command.isServerCommand());
        return command;
    };

    public Function<Role, RoleDTO> roleToRoleDTOFunction = role -> {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleID(role.getRoleID());
        roleDTO.setName(role.getName());
        return roleDTO;
    };

    public Function<RoleDTO, Role> roleDTOToRoleFunction = roleDTO -> {
        Role role = new Role();
        role.setRoleID(roleDTO.getRoleID());
        role.setName(roleDTO.getName());
        return role;
    };

    public Function<User, UserDTO> userToUserDTOFunction = user -> {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserID(user.getUserID());
        userDTO.setName(user.getName());
        userDTO.setTimeJoined(user.getTimeJoined());
        return userDTO;
    };

    public Function<UserDTO, User> userDTOToUserFunction = userDTO -> {
        User user = new User();
        user.setUserID(userDTO.getUserID());
        user.setName(userDTO.getName());
        user.setTimeJoined(userDTO.getTimeJoined());
        return user;
    };

    public Function<ReactionRole, ReactionRoleDTO> reactionRoleToReactionRoleDTOFunction = reactionRole -> {
        ReactionRoleDTO reactionRoleDTO = new ReactionRoleDTO();
        reactionRoleDTO.setEmojiID(reactionRole.getEmojiID());
        reactionRoleDTO.setRoleID(roleToRoleDTOFunction.apply(reactionRole.getRoleID()));
        reactionRoleDTO.setServerID(reactionRole.getServerID());
        return reactionRoleDTO;
    };

    public Function<ReactionRoleDTO, ReactionRole> reactionRoleDTOToReactionRoleFunction = reactionRoleDTO -> {
        ReactionRole reactionRole = new ReactionRole();
        reactionRole.setEmojiID(reactionRoleDTO.getEmojiID());
        reactionRole.setRoleID(roleDTOToRoleFunction.apply(reactionRoleDTO.getRoleID()));
        reactionRole.setServerID(reactionRoleDTO.getServerID());
        return reactionRole;
    };

    public Function<RoleToName, RoleToNameDTO> roleToNameTORoleToNameDTOFunction = roleToName -> {
        RoleToNameDTO roleToNameDTO = new RoleToNameDTO();
        roleToNameDTO.setRole(roleToRoleDTOFunction.apply(roleToName.getRole()));
        roleToNameDTO.setUser(userToUserDTOFunction.apply(roleToName.getUser()));
        return roleToNameDTO;
    };

    public Function<RoleToNameDTO, RoleToName> roleToNameDTOTORoleToNameFunction = roleToNameDTO -> {
        RoleToName roleToName = new RoleToName();
        roleToName.setRole(roleDTOToRoleFunction.apply(roleToNameDTO.getRole()));
        roleToName.setUser(userDTOToUserFunction.apply(roleToNameDTO.getUser()));
        return roleToName;
    };

    public Function<Log, LogDTO> logToLogDTOFunction = log -> {
        LogDTO logDTO = new LogDTO();
        logDTO.setCommand(commandToCommandDTOFunction.apply(log.getCommand()));
        logDTO.setTime(log.getTime());
        logDTO.setId(log.getId());
        logDTO.setUser(userToUserDTOFunction.apply(log.getUser()));
        return logDTO;
    };

    public Function<LogDTO, Log> logDTOToLogFunction = logDTO -> {
        Log log = new Log();
        log.setCommand(commandDTOToCommandFunction.apply(logDTO.getCommand()));
        log.setTime(log.getTime());
        log.setId(log.getId());
        log.setUser(userDTOToUserFunction.apply(logDTO.getUser()));
        return log;
    };
}
