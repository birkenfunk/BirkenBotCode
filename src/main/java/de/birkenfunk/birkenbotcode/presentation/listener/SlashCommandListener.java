package de.birkenfunk.birkenbotcode.presentation.listener;

import java.time.OffsetDateTime;
import java.util.Objects;

import de.birkenfunk.birkenbotcode.presentation.Message;
import de.birkenfunk.birkenbotcode.presentation.audio_player.PlayerManager;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashCommandListener extends ListenerAdapter{

	private MessageEmbed messageEmbed;

	@Override
	public void onSlashCommand(SlashCommandEvent event) {
		String command = event.getName();
		Message message = new Message(event.getMember(),
				event.getSubcommandName(),
				event.getGuild(),
				event.getTextChannel());
		mangementCommands(command, message);
		musicCommands(command, message);
		new MessageEmbed(null, "Test", "Test", EmbedType.UNKNOWN, null,0,null,null,null,null,null,null,null);
		event.replyEmbeds(messageEmbed).setEphemeral(true).queue();
	}
	
	/**
	 * Method for checking for a music command
	 * @param command Name of the command
	 * @param message Message of the command
	 */
	private void mangementCommands(String command, Message message) {
		if (message.getMember()!=null && message.getMember().hasPermission(Permission.ADMINISTRATOR)) {
			if(command.equalsIgnoreCase("server-info")) {//Should give different information about the Server
				message.getChannel().sendMessage("Work in Progress").queue();
			}
			if(command.equalsIgnoreCase("write-member")){ //Puts all Members of a Server into a Database
				//writeMember(con,event,guild);
			}
		}
	}
	
	/**
	 * Method for checking for a music command
	 * @param command Name of the command
	 * @param message Message of the command
	 */
	private void musicCommands(String command, Message message) {
		PlayerManager playerManager = PlayerManager.getManager();
		if (command.equalsIgnoreCase("play"))
			messageEmbed = playerManager.play(message);
		if (command.equalsIgnoreCase("leave"))
			messageEmbed = playerManager.leave(message);
		if(command.equalsIgnoreCase("skip"))
			messageEmbed = playerManager.skipTrack(message);
		if(command.equalsIgnoreCase("pause"))
			messageEmbed = playerManager.pauseTrack(message);
		if(command.equalsIgnoreCase("pause"))
			messageEmbed = playerManager.pauseTrack(message);
		if(command.equalsIgnoreCase("volume"))
			messageEmbed = playerManager.volume(0, message);
		if(command.equalsIgnoreCase("repeat"))
			messageEmbed = playerManager.repeat(message, true);
		if(command.equalsIgnoreCase("loop"))
			messageEmbed = playerManager.repeat(message, false);
		if(command.equalsIgnoreCase("clear"))
			messageEmbed = playerManager.clear(message);
		if(command.equalsIgnoreCase("shuffle"))
			messageEmbed = playerManager.shuffle(message);
	}
	
}
