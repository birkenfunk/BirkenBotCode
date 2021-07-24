package de.birkenfunk.birkenbotcode.persistent;

import de.birkenfunk.birkenbotcode.application.IDatabase;
import de.birkenfunk.birkenbotcode.domain.*;
import de.birkenfunk.birkenbotcode.persistent.entity.*;
import de.birkenfunk.birkenbotcode.persistent.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SqlConnector implements IDatabase {

    @Autowired
    private CommandRepo commandRepo;

    @Autowired
    private LogRepo logRepo;

    @Autowired
    private ReactionRoleRepo reactionRoleRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private RoleToNameRepo roleToNameRepo;

    @Autowired
    private UserRepo userRepo;

    private DTOMapper mapper = new DTOMapper();

    @Override
    public void saveRole(RoleDTO role) {
        roleRepo.save(mapper.roleDTOToRoleFunction.apply(role));
    }

    @Override
    public RoleDTO getRole(long id) {
        Optional<Role> role = roleRepo.findById(id);
        if(role.isEmpty())
            return null;
        return mapper.roleToRoleDTOFunction.apply(role.get());
    }

    @Override
    public void removeRole(long id) {
        Optional<Role> role = roleRepo.findById(id);
        if(role.isEmpty())
            return;
        roleRepo.delete(role.get());
    }

    @Override
    public List<RoleDTO> getRoles() {
        return roleRepo.findAll().stream().map(mapper.roleToRoleDTOFunction).collect(Collectors.toList());
    }

    @Override
    public void saveUser(UserDTO user) {
        userRepo.save(mapper.userDTOToUserFunction.apply(user));
    }

    @Override
    public void removeUser(long id) {
        Optional<User> user = userRepo.findById(id);
        if(user.isEmpty())
            return;
        userRepo.delete(user.get());
    }

    @Override
    public UserDTO getUser(long id) {
        Optional<User> user = userRepo.findById(id);
        if(user.isEmpty())
            return null;
        return mapper.userToUserDTOFunction.apply(user.get());
    }

    @Override
    public List<UserDTO> getUsers() {
        return userRepo.findAll().stream().map(mapper.userToUserDTOFunction).collect(Collectors.toList());
    }

    @Override
    public void saveCommand(CommandDTO command) {
        commandRepo.save(mapper.commandDTOToCommandFunction.apply(command));
    }

    @Override
    public void removeCommand(int id) {
        Optional<Command> command = commandRepo.findById(id);
        if(command.isEmpty())
            return;
        commandRepo.delete(command.get());
    }

    @Override
    public CommandDTO getCommand(int id) {
        Optional<Command> command = commandRepo.findById(id);
        if(command.isEmpty())
            return null;
        return mapper.commandToCommandDTOFunction.apply(command.get());
    }

    @Override
    public List<CommandDTO> getCommands() {
        return commandRepo.findAll().stream().map(mapper.commandToCommandDTOFunction).collect(Collectors.toList());
    }

    @Override
    public void saveReactionRole(ReactionRoleDTO reactionRole) {
        reactionRoleRepo.save(mapper.reactionRoleDTOToReactionRoleFunction.apply(reactionRole));
    }

    @Override
    public void removeReactionRole(ReactionRoleID id) {
        Optional<ReactionRole> reactionRole = reactionRoleRepo.findById(id);
        if(reactionRole.isEmpty())
            return;
        reactionRoleRepo.delete(reactionRole.get());
    }

    @Override
    public ReactionRoleDTO getReactionRole(ReactionRoleID reactionID) {
        Optional<ReactionRole> reactionRole = reactionRoleRepo.findById(reactionID);
        if(reactionRole.isEmpty())
            return null;
        return mapper.reactionRoleToReactionRoleDTOFunction.apply(reactionRole.get());
    }

    @Override
    public List<ReactionRoleDTO> getReactionRoles() {
        return reactionRoleRepo.findAll().stream().map(mapper.reactionRoleToReactionRoleDTOFunction).collect(Collectors.toList());
    }

    @Override
    public void addLog(LogDTO log) {
        logRepo.save(mapper.logDTOToLogFunction.apply(log));
    }

    @Override
    public void addUserToRole(long userID, long roleID, long serverID) {
        RoleToName roleToName = new RoleToName();
        roleToName.setRole(roleRepo.getById(roleID));
        roleToName.setUser(userRepo.getById(userID));
        roleToName.setServerId(serverID);
        roleToNameRepo.save(roleToName);
    }

    @Override
    public void removeUserFromRole(long userID, long roleID, long serverID) {
        RoleToNameId roleToNameId = new RoleToNameId();
        roleToNameId.setRole(roleRepo.getById(roleID));
        roleToNameId.setUser(userRepo.getById(userID));
        roleToNameId.setServerId(serverID);
        Optional<RoleToName> roleToName = roleToNameRepo.findById(roleToNameId);
        if(roleToName.isEmpty())
            return;
        roleToNameRepo.delete(roleToName.get());
    }

    @Override
    public List<RoleToNameDTO> getRolesToName(long nameID) {
        List<RoleToNameDTO> res = new LinkedList<>();
        for (RoleToNameDTO roleToNameDTO: getAllNamesToRoles()) {
            if(roleToNameDTO.getUser().getUserID()==nameID)
                res.add(roleToNameDTO);
        }
        return res;
    }

    @Override
    public List<RoleToNameDTO> getNamesToRole(long roleID) {
        List<RoleToNameDTO> res = new LinkedList<>();
        for (RoleToNameDTO roleToNameDTO: getAllNamesToRoles()) {
            if(roleToNameDTO.getRole().getRoleID()==roleID)
                res.add(roleToNameDTO);
        }
        return res;
    }

    @Override
    public List<RoleToNameDTO> getAllNamesToRoles() {
        return roleToNameRepo.findAll().stream().map(mapper.roleToNameTORoleToNameDTOFunction).collect(Collectors.toList());
    }

    @Override
    public List<LogDTO> getLog() {
        return logRepo.findAll().stream().map(mapper.logToLogDTOFunction).collect(Collectors.toList());
    }
}
