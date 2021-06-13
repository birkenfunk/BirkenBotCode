package de.birkenfunk.birkenbotcode.presentation.listener;

import de.birkenfunk.birkenbotcode.persistent.MysqlCon;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Class for different Listeners for a Server
 * @author Alexander Asbeck
 * @version 1.5
 */
public class EventListener extends ListenerAdapter {
    private static final Logger LOGGER = LogManager.getLogger(EventListener.class);
    /* todo Documentation */

    private final MysqlCon con = MysqlCon.getMysqlCon();

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        try {
            con.writeToMember(event.getMember().getIdLong(),event.getMember().getUser().getName());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        try {
            con.removeUserFromDB(event.getUser().getIdLong());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    public void onGuildMemberRoleAdd(@NotNull GuildMemberRoleAddEvent event) {
        List<Role> roleList=event.getRoles();
        try {
        for (Role role : roleList) {
                con.writeUserIDRoleID(event.getMember().getIdLong(), role.getIdLong());
        }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

    }

    @Override
    public void onGuildMemberRoleRemove(@NotNull GuildMemberRoleRemoveEvent event) {
        List<Role> roleList=event.getRoles();
        try {
            for (Role role : roleList) {
                con.removeUserFromRole(event.getMember().getIdLong(), role.getIdLong());
            }
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    public void onRoleCreate(@NotNull RoleCreateEvent event) {
        try {
            con.writeToRole(event.getRole().getIdLong(),event.getRole().getName());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    public void onRoleDelete(@NotNull RoleDeleteEvent event) {
        try {
            con.removeRoleFromDB(event.getRole().getIdLong());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}

