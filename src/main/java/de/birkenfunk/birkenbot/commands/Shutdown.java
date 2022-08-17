package de.birkenfunk.birkenbot.commands;

import de.birkenfunk.birkenbot.ConfigReader;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import one.util.streamex.StreamEx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

public class Shutdown extends ListenerAdapter {

    private final Logger log = LogManager.getLogger(Shutdown.class);

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getChannelType() != ChannelType.PRIVATE || !event.getMessage().getContentRaw().startsWith("!")) {
            return;
        }
        log.trace("Received message: {}", event.getMessage().getContentDisplay());
        boolean isAdmin = StreamEx.of(ConfigReader.getAdminUserIDs()).has(String.valueOf(event.getAuthor().getIdLong()));
        if (event.getMessage().getContentRaw().equals("!shutdown")) {
            if (isAdmin) {
                log.debug("Shutdown command received");
                event.getMessage().reply("Shutting down...").queue();
                event.getJDA().shutdown();
            } else {
                log.debug("User {} Tryed to Use an Admin Command", event.getAuthor().getName());
                event.getMessage().reply("You are not allowed to use this command.").queue();
            }
        }else {
            event.getMessage().reply("Possible Commands: !shutdown").queue();
        }
    }
}
