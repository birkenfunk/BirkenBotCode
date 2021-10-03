package de.birkenfunk.birkenbotcode.persistent;

import de.birkenfunk.birkenbotcode.application.IDatabase;
import de.birkenfunk.birkenbotcode.domain.*;
import de.birkenfunk.birkenbotcode.persistent.entity.*;
import de.birkenfunk.birkenbotcode.persistent.exceptions.RoleNotFoundException;
import de.birkenfunk.birkenbotcode.persistent.exceptions.UserNotFoundException;
import de.birkenfunk.birkenbotcode.persistent.repos.*;
import one.util.streamex.StreamEx;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    private UserRepo userRepo;

    private final ModelMapper mapper = new ModelMapper();

    @Override
    public void saveRole(RoleDTO role) {
        roleRepo.save(mapper.map(role, Role.class));
    }

    @Override
    public void saveRoles(List<RoleDTO> roles) {
        roleRepo.saveAll(StreamEx.of(roles).map(it -> mapper.map(it, Role.class)).toList());
    }

    @Override
    public RoleDTO getRole(long id) {
        Optional<Role> role = roleRepo.findById(id);
        if (role.isEmpty())
            return null;
        return mapper.map(role.get(), RoleDTO.class);
    }

    @Override
    public void removeRole(long id) {
        Optional<Role> role = roleRepo.findById(id);
        if (role.isEmpty())
            return;
        roleRepo.delete(role.get());
    }

    @Override
    public List<RoleDTO> getRoles() {
        return StreamEx.of(roleRepo.findAll()).map(it -> mapper.map(it, RoleDTO.class)).toList();
    }

    @Override
    public void saveUser(UserDTO user) {
        userRepo.save(mapper.map(user, User.class));
    }

    @Override
    public void saveUsers(List<UserDTO> users) {
        userRepo.saveAll(StreamEx.of(users).map(it -> mapper.map(it, User.class)).toList());
    }

    @Override
    public void removeUser(long id) {
        Optional<User> user = userRepo.findById(id);
        if (user.isEmpty())
            return;
        userRepo.delete(user.get());
    }

    @Override
    public UserDTO getUser(long id) {
        Optional<User> user = userRepo.findById(id);
        if (user.isEmpty())
            return null;
        return mapper.map(user.get(), UserDTO.class);
    }

    @Override
    public List<UserDTO> getUsers() {
        return StreamEx.of(userRepo.findAll()).map(it -> mapper.map(it, UserDTO.class)).toList();
    }

    @Override
    public void saveCommand(CommandDTO command) {
        commandRepo.save(mapper.map(command, Command.class));
    }

    @Override
    public void removeCommand(int id) {
        Optional<Command> command = commandRepo.findById(id);
        if (command.isEmpty())
            return;
        commandRepo.delete(command.get());
    }

    @Override
    public CommandDTO getCommand(int id) {
        Optional<Command> command = commandRepo.findById(id);
        if (command.isEmpty())
            return null;
        return mapper.map(command.get(), CommandDTO.class);
    }

    @Override
    public List<CommandDTO> getCommands() {
        return StreamEx.of(commandRepo.findAll()).map(it -> mapper.map(it, CommandDTO.class)).toList();
    }

    @Override
    public void saveReactionRole(ReactionRoleDTO reactionRole) {
        reactionRoleRepo.save(mapper.map(reactionRole, ReactionRole.class));
    }

    @Override
    public void removeReactionRole(ReactionRoleID id) {
        Optional<ReactionRole> reactionRole = reactionRoleRepo.findById(id);
        if (reactionRole.isEmpty())
            return;
        reactionRoleRepo.delete(reactionRole.get());
    }

    @Override
    public ReactionRoleDTO getReactionRole(ReactionRoleID reactionID) {
        Optional<ReactionRole> reactionRole = reactionRoleRepo.findById(reactionID);
        if (reactionRole.isEmpty())
            return null;
        return mapper.map(reactionRole.get(), ReactionRoleDTO.class);
    }

    @Override
    public List<ReactionRoleDTO> getReactionRoles() {
        return StreamEx.of(reactionRoleRepo.findAll()).map(it -> mapper.map(it, ReactionRoleDTO.class)).toList();
    }

    @Override
    public void addLog(LogDTO log) {
        logRepo.save(mapper.map(log, Log.class));
    }

    @Override
    public void addUserToRole(long userID, long roleID) throws UserNotFoundException, RoleNotFoundException {
        Optional<User> optionalUser = userRepo.findById(userID);
        Optional<Role> optionalRole = roleRepo.findById(roleID);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User with Id: " + userID + "was not Found");
        }
        if (optionalRole.isEmpty()) {
            throw new RoleNotFoundException("Role with Id: " + roleID + "was not Found");
        }
        Role role = optionalRole.get();
        User user = optionalUser.get();
        role.addUserToRole(user);
        user.addRoleToUser(role);
        roleRepo.save(role);
        userRepo.save(user);
    }

    @Override
    public void removeUserFromRole(long userID, long roleID) throws UserNotFoundException, RoleNotFoundException {
        Optional<User> optionalUser = userRepo.findById(userID);
        Optional<Role> optionalRole = roleRepo.findById(roleID);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User with Id: " + userID + "was not Found");
        }
        if (optionalRole.isEmpty()) {
            throw new RoleNotFoundException("Role with Id: " + roleID + "was not Found");
        }
        Role role = optionalRole.get();
        User user = optionalUser.get();
        role.removeUserFromRole(user);
        user.removeRoleFromUser(role);
        roleRepo.save(role);
        userRepo.save(user);
    }

    @Override
    public List<LogDTO> getLog() {
        return StreamEx.of(logRepo.findAll()).map(it -> mapper.map(it, LogDTO.class)).toList();
    }
}
