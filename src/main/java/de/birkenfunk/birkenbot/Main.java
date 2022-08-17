package de.birkenfunk.birkenbot;

import de.birkenfunk.birkenbot.commands.Hello;
import de.birkenfunk.birkenbot.commands.Shutdown;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;

public class Main {

    private final static Logger LOG = LogManager.getLogger(Main.class);
    public static void main(String[] args) {
        new Main();
    }

    private Main() {
        LOG.debug("Starting Birkenbot");
        String botToken = System.getenv("BOT_TOKEN");
        if(botToken == null) {
            LOG.error("No BOT_TOKEN environment variable found. Please set it.");
            System.exit(1);
        }
        JDABuilder builder = JDABuilder.createDefault(botToken);
        builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
        builder.addEventListeners(new Shutdown(), new Hello());
        JDA jda = null;
        try {
            jda = builder.build().awaitReady();
        } catch (InterruptedException e) {
            LOG.error("InterruptedException while waiting for JDA to be ready", e);
            System.exit(1);
        } catch (LoginException e) {
            LOG.error("LoginException while waiting for JDA to be ready", e);
            System.exit(1);
        }
        initCommands(jda);
    }

    private void initCommands(@NotNull JDA jda) {
        jda.upsertCommand("hello","Greets Back").queue();
    }
}