package de.birkenfunk.birkenbotcode.presentation.listener;

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

	private final char prefix = '/';

	/**
	 * Reacts to a Message on a Server
	 * @param event A event that should be handled
	 */
	@Override
	public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
		String message = event.getMessage().getContentDisplay();
		if(message.isEmpty()||message.charAt(0) != prefix||event.getAuthor().isBot())
			return;
		//MysqlCon con= MysqlCon.getMysqlCon();
		Guild guild = event.getGuild();
		String[] splittedMessage = message.split(" ");
		String command = splittedMessage[0];
		if (event.getMember()!=null && event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
			try {
				if(command.equalsIgnoreCase(prefix+"server-info")) {//Should give different information about the Server
					event.getChannel().sendMessage("Work in Progress").queue();
				}
				if(command.equalsIgnoreCase(prefix+"write-member")){ //Puts all Members of a Server into a Database
					//writeMember(con,event,guild);
				}
			} catch (Exception e) {
				error(event.getChannel());
			}
		}
		if(splittedMessage[0].equalsIgnoreCase(prefix+"help")){//prints out all available Commands
			List<Command> commandList= null;
			try {
				//con.writeToLog("help",event.getAuthor());
				//commandList = con.getCommands();
			} catch (Exception e) {
				error(event.getChannel());
			}
			if(commandList!=null){
				StringBuilder builder = new StringBuilder();
				for (Command commands : commandList) {
					if (commands.isServerCommand())
						builder.append(prefix).append(commands.getName()).append("	").append(commands.getDescription()).append("\n");
				}
				event.getChannel().sendMessage(builder.toString()).queue();
			}
		}
	}

	/**
	 * Writes Roles and Members to Database
	 * @param event {@link GuildMessageReceivedEvent}
	 * @param guild {@link Guild}
	 * @throws Exception If an Error with the MySQL Database happens
	 */
	private void writeMember(GuildMessageReceivedEvent event,Guild guild) throws Exception {
		/*con.writeToLog("write-member", Objects.requireNonNull(event.getMember()).getUser());
		guild.loadMembers();
		List<Role> serverRoles = guild.getRoles();
		List<Role> roles = new LinkedList<>(serverRoles);
		con.writeToRole(roles);
		List<Member> serverMembers = guild.getMembers();
		List<Member> members = new LinkedList<>();
		List<Long> userIDs = new LinkedList<>();
		List<Long> roleIDs = new LinkedList<>();
		for (Member currentMember : serverMembers) {
			members.add(currentMember);
			List<Role> userRole = currentMember.getRoles();
			for (Role role : userRole) {
				userIDs.add(currentMember.getIdLong());
				roleIDs.add(role.getIdLong());
			}
		}
		con.writeToMember(members);
		con.writeUserIDRoleID(userIDs, roleIDs);
		event.getChannel().sendMessage(serverMembers.size() + " have been recognised. If there are more members on your Server try again.").queue();
	*/}

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
			/*try {
				if(command.equalsIgnoreCase(prefix+"exit")) {//Closes the Bot
					MysqlCon.getMysqlCon().writeToLog("exit", event.getAuthor());
					event.getChannel().sendMessage("Bot Shutdown").queue();
					event.getJDA().getPresence().setStatus(OnlineStatus.OFFLINE);
					event.getJDA().shutdown();
					//DiscordBot.getDiscordBot().stopThreadListener();
				}
				if(command.equalsIgnoreCase(prefix+"play")) {//Changes Activity of Bot
					MysqlCon.getMysqlCon().writeToLog("play", event.getAuthor());
					ActivityManager.setActivity(Activities.PLAYING, event);
				}
				if(command.equalsIgnoreCase(prefix+"watch")) {//Changes Activity of Bot
					MysqlCon.getMysqlCon().writeToLog("play", event.getAuthor());
					ActivityManager.setActivity(Activities.WATCHING, event);
				}
				if(command.equalsIgnoreCase(prefix+"listen")) {//Changes Activity of Bot
					MysqlCon.getMysqlCon().writeToLog("play", event.getAuthor());
					ActivityManager.setActivity(Activities.LISTENING, event);
				}
			} catch (Exception e) {
				error(event.getChannel());
				e.printStackTrace();
			}*/
		}
		if (isChat(message)) {//temporary disabled due to no development at the moment
			//event.getChannel().sendMessage(ChatBot.getResponse(message)).queue();
		}
	}

	private void error(MessageChannel channel){
		channel.sendMessage("MySql Connection went wrong please contact the Developer on birkenfunk@outlook.de").queue();
	}
}
