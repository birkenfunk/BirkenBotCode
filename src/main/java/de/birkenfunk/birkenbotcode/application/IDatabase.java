package de.birkenfunk.birkenbotcode.application;

import de.birkenfunk.birkenbotcode.domain.*;

import java.util.List;

public interface IDatabase {

    void saveRole(RoleDTO role);

    RoleDTO getRole(int id);

    List<RoleDTO> getRoles();

    void saveUser(UserDTO user);

    UserDTO getUser(int id);

    List<UserDTO> getUsers();

    void saveCommand(CommandDTO command);

    CommandDTO getCommand(int id);

    List<CommandDTO> getCommands();

    void saveReactionRole(ReactionRoleDTO reactionRole);

    ReactionRoleDTO getReactionRole(int reactionID);

    List<ReactionRoleDTO> getReactionRoles();

    void addLog(LogDTO log);

    void addUserToRole(int userID, int roleID);

    List<RoleToNameDTO> getRolesToName(int nameID);

    List<RoleToNameDTO> getNamesToRole(int roleID);

    List<RoleToNameDTO> getAllNamesToRoles();

    List<LogDTO> getLog();
}
