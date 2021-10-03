package de.birkenfunk.birkenbotcode.application;

import de.birkenfunk.birkenbotcode.domain.*;
import de.birkenfunk.birkenbotcode.persistent.entity.ReactionRoleID;
import de.birkenfunk.birkenbotcode.persistent.exceptions.RoleNotFoundException;
import de.birkenfunk.birkenbotcode.persistent.exceptions.UserNotFoundException;

import java.util.List;

public interface IDatabase {

    void saveRole(RoleDTO role);

    public void saveRoles(List<RoleDTO> roles);

    void removeRole(long id);

    RoleDTO getRole(long id);

    List<RoleDTO> getRoles();

    void saveUser(UserDTO user);

    public void saveUsers(List<UserDTO> users);

    void removeUser(long id);

    UserDTO getUser(long id);

    List<UserDTO> getUsers();

    void saveCommand(CommandDTO command);

    void removeCommand(int id);

    CommandDTO getCommand(int id);

    List<CommandDTO> getCommands();

    void saveReactionRole(ReactionRoleDTO reactionRole);

    void removeReactionRole(ReactionRoleID id);

    ReactionRoleDTO getReactionRole(ReactionRoleID reactionID);

    List<ReactionRoleDTO> getReactionRoles();

    void addLog(LogDTO log);

    void addUserToRole(long userID, long roleID) throws UserNotFoundException, RoleNotFoundException;

    void removeUserFromRole(long userID, long roleID) throws UserNotFoundException, RoleNotFoundException;

    List<RoleToNameDTO> getRolesToName(long nameID);

    List<RoleToNameDTO> getNamesToRole(long roleID);

    List<RoleToNameDTO> getAllNamesToRoles();

    List<LogDTO> getLog();
}
