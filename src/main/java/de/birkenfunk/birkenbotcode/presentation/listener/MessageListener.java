package de.birkenfunk.birkenbotcode.presentation.listener;

import de.birkenfunk.birkenbotcode.DiscordBot;
import de.birkenfunk.birkenbotcode.presentation.audio_player.PlayerManager;
import de.birkenfunk.birkenbotcode.domain.helper_classes.Command;
import de.birkenfunk.birkenbotcode.presentation.activity.ActivityManager;
import de.birkenfunk.birkenbotcode.domain.enums.Activities;
import de.birkenfunk.birkenbotcode.application.mixins.AccessMixins;
import de.birkenfunk.birkenbotcode.application.mixins.CommandFormatMixins;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Class reacts to messages
 * @author Alexander Asbeck
 * @version 2.0
 */
public class MessageListener extends ListenerAdapter implements AccessMixins, CommandFormatMixins {

	private final char prefix = '!'; 
	/**
	 * Reacts to a Message in a Privat Chat
	 * @param event A event that should be handled
	 */
	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		String message = event.getMessage().getContentDisplay();
		String authorName = event.getAuthor().toString();
		String[] splittedMessage = message.split(" ");
		StringBuilder stringBuffer = new StringBuilder();

		for (int i= 1;splittedMessage.length-1>=i;i++)
			stringBuffer.append(splittedMessage[i]).append(" ");
		stringBuffer.append(splittedMessage[splittedMessage.length-1]);

		if(message.isEmpty() || isBirkenBot(authorName)||message.charAt(0) != prefix) {
			return;
		}

		if (hasAdminAccess(authorName)) {
			String command = message.split(" ")[0];
			try {
				if(command.equalsIgnoreCase(prefix+"exit")) {//Closes the Bot
					event.getChannel().sendMessage("Bot Shutdown").queue();
					event.getJDA().getPresence().setStatus(OnlineStatus.OFFLINE);
					event.getJDA().shutdown();
					System.exit(0);
				}
				if(command.equalsIgnoreCase(prefix+"play")) {//Changes Activity of Bot
					ActivityManager.setActivity(Activities.PLAYING, event);
				}
				if(command.equalsIgnoreCase(prefix+"watch")) {//Changes Activity of Bot
					ActivityManager.setActivity(Activities.WATCHING, event);
				}
				if(command.equalsIgnoreCase(prefix+"listen")) {//Changes Activity of Bot
					ActivityManager.setActivity(Activities.LISTENING, event);
				}
			} catch (Exception e) {
				error(event.getChannel());
				e.printStackTrace();
			}
		}
		if (isChat(message)) {//temporary disabled due to no development at the moment
			//event.getChannel().sendMessage(ChatBot.getResponse(message)).queue();
		}
	}

	private void error(MessageChannel channel){
		channel.sendMessage("MySql Connection went wrong please contact the Developer on birkenfunk@outlook.de").queue();
	}
}
