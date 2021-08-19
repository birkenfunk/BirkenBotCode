package de.birkenfunk.birkenbotcode.domain;

import java.util.Set;

public class RoleDTO {

    private long roleID;
    private String name;
    private Set<UserDTO> users;

    public long getRoleID() {
        return roleID;
    }

    public void setRoleID(long roleID) {
        this.roleID = roleID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsers(Set<UserDTO> users) {
        this.users = users;
    }

    public void addUser(UserDTO userToAdd){

    }
}
