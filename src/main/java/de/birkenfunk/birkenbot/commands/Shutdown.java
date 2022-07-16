package de.birkenfunk.birkenbot.commands;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Shutdown extends ListenerAdapter {

    private final Logger log = LogManager.getLogger(Shutdown.class);

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.PRIVATE) && event.getMessage().getContentRaw().equals("!shutdown")) {
            log.debug("Shutdown command received");
            event.getMessage().reply("Shutting down...").queue();
            event.getJDA().shutdown();
        }
    }
}
