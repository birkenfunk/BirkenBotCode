package de.birkenfunk.birkenbotcode.presentation.listener;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import de.birkenfunk.birkenbotcode.application.IDatabase;
import de.birkenfunk.birkenbotcode.domain.RoleDTO;
import de.birkenfunk.birkenbotcode.domain.UserDTO;
import de.birkenfunk.birkenbotcode.presentation.Message;
import de.birkenfunk.birkenbotcode.presentation.audio_player.PlayerManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class SlashCommandListener extends ListenerAdapter{

	private MessageEmbed messageEmbed;

	private IDatabase database;

	public SlashCommandListener(IDatabase database) {
		this.database = database;
	}

	@Override
	public void onSlashCommand(SlashCommandEvent event) {
		String command = event.getName();
		Message message = new Message(event.getMember(),
				event.getSubcommandName(),
				event.getGuild(),
				event.getTextChannel());
		event.deferReply().queue();
		Thread t1 = new Thread(() -> {mangementCommands(command, message);
			if(messageEmbed!= null)
				event.getInteraction().getHook().editOriginalEmbeds(messageEmbed).queue();
		});
		Thread t2 = new Thread(() -> {
			musicCommands(command, message);
			if (messageEmbed != null)
				event.getInteraction().getHook().editOriginalEmbeds(messageEmbed).queue();
		});
		t1.start();
		t2.start();
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
				message.getGuild().loadMembers();
				List<Member> members = new LinkedList<>(message.getGuild().getMembers());
				List<Role> roles = new LinkedList<>(message.getGuild().getRoles());
				roles.stream().map(roleToRoleDTO).forEach(role -> database.saveRole(role));
				members.stream().map(memberToUserDTO).forEach(user -> database.saveUser(user));
				members.forEach(member -> //Adds a user to a role
					member.getRoles().forEach(
							role -> database.addUserToRole(member.getIdLong(),
									role.getIdLong(), 
									member.getGuild().getIdLong()))
				);
				messageEmbed = simpleMessageBuilder("Info", "Added "+ members.size()+ "to the Database");
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
	
	
	Function<Member, UserDTO> memberToUserDTO = new Function<Member, UserDTO>() {
		
		@Override
		public UserDTO apply(Member t) {
			UserDTO user = new UserDTO();
			user.setName(t.getEffectiveName());
			user.setUserID(t.getIdLong());
			user.setTimeJoined(t.getTimeJoined());
			return user;
		}
	};
	
	Function<Role, RoleDTO> roleToRoleDTO = new Function<Role, RoleDTO>() {
		
		@Override
		public RoleDTO apply(Role t) {
			RoleDTO role = new RoleDTO();
			role.setName(t.getName());
			role.setRoleID(t.getIdLong());
			return role;
		}
	};
	
	private MessageEmbed simpleMessageBuilder(String title, String description){
        return new MessageEmbed("",
                title,
                description,
                EmbedType.RICH,
                null,
                255,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
    }
}
