package de.birkenfunk.birkenbotcode.persistent.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Role {

    private long roleID;
    private String name;
    private Set<User> users = new HashSet<>();

    @Id
    @Column(name = "role_id", updatable = false, nullable = false)
    public long getRoleID() {
        return roleID;
    }

    public void setRoleID(long roleID) {
        this.roleID = roleID;
    }

    @Basic
    @Column(name = "role_name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void addUserToRole(User userToAdd){
        users.add(userToAdd);
    }

    public void removeUserFromRole(User userToRemove){
        users.remove(userToRemove);
    }
}
