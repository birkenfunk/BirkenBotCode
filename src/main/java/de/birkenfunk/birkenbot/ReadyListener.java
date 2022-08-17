package de.birkenfunk.birkenbot;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReadyListener implements EventListener {

    private final Logger log = LogManager.getLogger(ReadyListener.class);

    @Override
    public void onEvent(GenericEvent event) {
        if (event instanceof ReadyEvent) {
            log.debug("Bot is ready");
        }
    }
}
