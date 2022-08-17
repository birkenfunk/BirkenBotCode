package de.birkenfunk.birkenbot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This Class is the implementation of the Hello Slash Command.
 * @author Birkenfunk
 */
public class Hello extends ListenerAdapter {

        private final Logger log = LogManager.getLogger(Hello.class);

        @Override
        public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
            if (event.getName().equals("hello")) {
                log.debug("Hello command received");
                event.reply("Hello " + event.getUser().getName() + "!").queue();
            }
        }
}
