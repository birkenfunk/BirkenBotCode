package de.birkenfunk.birkenbotcode.presentation.listener;

import java.util.Objects;

import de.birkenfunk.birkenbotcode.presentation.Message;
import de.birkenfunk.birkenbotcode.presentation.audio_player.PlayerManager;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashCommandListener extends ListenerAdapter{
	
	@Override
	public void onSlashCommand(SlashCommandEvent event) {
		PlayerManager playerManager = PlayerManager.getManager();
		String command = event.getName();
		Message message = new Message(event.getMember(),
				event.getSubcommandName(),
				event.getGuild(),
				event.getTextChannel());
		if (command.equalsIgnoreCase("play"))
			playerManager.play(message);
		if (command.equalsIgnoreCase("leave")) 
			playerManager.leave(message);
		if(command.equalsIgnoreCase("skip"))
			playerManager.skipTrack(message);
		if(command.equalsIgnoreCase("pause"))
			playerManager.pauseTrack(message);
		if(command.equalsIgnoreCase("pause"))
			playerManager.pauseTrack(message);
		if(command.equalsIgnoreCase("volume"))
			playerManager.volume(0, message);
		if(command.equalsIgnoreCase("repeat"))
			playerManager.repeat(message, true);
		if(command.equalsIgnoreCase("loop"))
			playerManager.repeat(message, false);
		if(command.equalsIgnoreCase("clear"))
			playerManager.clear(message);
		if(command.equalsIgnoreCase("shuffle"))
			playerManager.shuffle(message);
		
	}
}
