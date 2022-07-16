package de.birkenfunk.birkenbot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.security.auth.login.LoginException;

public class Main {

    private final static Logger LOG = LogManager.getLogger(Main.class);
    public static void main(String[] args) {
        String botToken = System.getenv("BOT_TOKEN");
        if(botToken == null) {
            LOG.error("No BOT_TOKEN environment variable found. Please set it.");
            System.exit(1);
        }
        JDABuilder builder = JDABuilder.createDefault(botToken);
        builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
        try {
            builder.build().awaitReady();
        } catch (InterruptedException e) {
            LOG.error("InterruptedException while waiting for JDA to be ready", e);
            System.exit(1);
        } catch (LoginException e) {
            LOG.error("LoginException while waiting for JDA to be ready", e);
            System.exit(1);
        }
        LOG.debug("Bot is ready");
    }
}