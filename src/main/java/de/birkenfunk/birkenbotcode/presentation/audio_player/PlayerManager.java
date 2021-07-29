package de.birkenfunk.birkenbotcode.presentation.audio_player;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.birkenfunk.birkenbotcode.presentation.Message;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {
   private static PlayerManager manager;
   private final AudioPlayerManager playerManager;
   private final Map<Long,GuildMusicManager> musicManagers;

    private PlayerManager() {
        musicManagers = new HashMap<>();
        playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    
    /**
     * Adds a new {@link GuildMusicManager} to the musicManagers map if it doesn't exists
     * @param guild {@link Guild} where to play music
     * @return {@link GuildMusicManager} form the musicManagers map
     */
    private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.computeIfAbsent(guildId,aLong -> new GuildMusicManager(playerManager));

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    /**
     * Adds a new track to the queue
     */
    public MessageEmbed loadAndPlay(Message message){
        Guild guild = message.getGuild();
        Member member = message.getMember();
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
        
        if(member.getVoiceState()!=null && !member.getVoiceState().inVoiceChannel()) {//if not in a channel
        	return simpleMessageBuilder("Warning", member.getAsMention() + " you have to be in a voice channel");
        }
        
        if(!isInSameChannel(message.getMember())&&!isNotConnected(guild)) {//already connected but not in same channel
            return sendSameChannel();
		}
        final MessageEmbed[] embed = new MessageEmbed[1];
        playerManager.loadItemOrdered(musicManager, message.getContent(), new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                embed[0] = simpleMessageBuilder("Info", "Adding to queue: " + track.getInfo().title);
                play(guild, musicManager, track, member);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();
                if(firstTrack == null)
                    firstTrack = playlist.getTracks().get(0);

                embed[0] = simpleMessageBuilder("Info", "Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")");
                
                play(guild, musicManager, firstTrack, member);
            }

            @Override
            public void noMatches() {
                embed[0] = simpleMessageBuilder("Warning","Nothing found by " + message.getContent());
            }

            @Override
            public void loadFailed(FriendlyException e) {
                embed[0] = simpleMessageBuilder("Warning", "Could not play: " + e.getMessage());
            }
        });
        return embed[0];
    }

    public MessageEmbed skipTrack(Message message){
        if(isNotConnected(message.getGuild()))
            return botNotConnected();
        if(isInSameChannel(message.getMember())){
        	return sendSameChannel();
        }
        GuildMusicManager musicManager = getGuildAudioPlayer(message.getChannel().getGuild());
        musicManager.getScheduler().nextTrack();
        return simpleMessageBuilder("Info", "Skipped to next track.");
    }

    public MessageEmbed pauseTrack(Message message){
        if(isNotConnected(message.getGuild()))
            return botNotConnected();
        if(isInSameChannel(message.getMember())){
            return sendSameChannel();
        }
        GuildMusicManager musicManager = getGuildAudioPlayer(message.getGuild());
        if(musicManager.getPlayer().isPaused()){
            musicManager.getPlayer().setPaused(false);
            return simpleMessageBuilder("Info", "Unpaused Player");
        }else {
            musicManager.getPlayer().setPaused(true);
            return simpleMessageBuilder("Info", "Paused Player");
        }
    }

    public MessageEmbed play(Message message){
        if(message.getContent()!=null)
            return loadAndPlay(message);
        if(isNotConnected(message.getGuild()))
            return botNotConnected();
        if(!(message.getMember().hasPermission(Permission.KICK_MEMBERS)||isInSameChannel(message.getMember()))) {
			return sendSameChannel();
		}
        GuildMusicManager musicManager = getGuildAudioPlayer(message.getGuild());
        musicManager.getPlayer().setPaused(false);
        return simpleMessageBuilder("Info", "Unpaused Player");
    }

    public MessageEmbed repeat(Message message, Boolean repeat){
        if(isNotConnected(message.getGuild()))
            return botNotConnected();
        if(!(message.getMember().hasPermission(Permission.KICK_MEMBERS)||isInSameChannel(message.getMember()))) {
			return sendSameChannel();
		}
        GuildMusicManager musicManager = getGuildAudioPlayer(message.getGuild());
        TrackScheduler trackScheduler = musicManager.getScheduler();
        if (Boolean.TRUE.equals(repeat)){
            if(trackScheduler.isRepeat1()){
                trackScheduler.setRepeat1(false);
                return simpleMessageBuilder("Info", "Song isn't in loop any more");
            }else {
                trackScheduler.setRepeat1(true);
                return simpleMessageBuilder("Info", "Song is in loop");
            }

        }else {
            if(trackScheduler.isRepeat()){
                trackScheduler.setRepeat(false);
                return simpleMessageBuilder("Info", "Playlist isn't in loop any more");
            }else {
                trackScheduler.setRepeat(true);
                return simpleMessageBuilder("Info", "Playlist is in loop");
            }
        }
    }

    public MessageEmbed volume(int volume, Message message){
        if(isNotConnected(message.getGuild()))
            return botNotConnected();
        if(!(message.getMember().hasPermission(Permission.KICK_MEMBERS)||isInSameChannel(message.getMember()))) {
			return sendSameChannel();
		}
        GuildMusicManager musicManager = getGuildAudioPlayer(message.getGuild());
        musicManager.getPlayer().setVolume(volume);
        return simpleMessageBuilder("Info", "Set Volume to "+volume+"%");
    }

    public MessageEmbed stop(Message message){
    	if(isNotConnected(message.getGuild()))
            return botNotConnected();
    	if(!(message.getMember().hasPermission(Permission.KICK_MEMBERS)||isInSameChannel(message.getMember()))) {
			return sendSameChannel();
		}
        GuildMusicManager musicManager = getGuildAudioPlayer(message.getGuild());
        musicManager.getScheduler().stop();
        return simpleMessageBuilder("Info", "Song has been Stopped");
    }

    public MessageEmbed clear(Message message){
    	if(isNotConnected(message.getGuild()))
            return botNotConnected();
    	if(!(message.getMember().hasPermission(Permission.KICK_MEMBERS)||isInSameChannel(message.getMember()))) {
			return sendSameChannel();
		}
        GuildMusicManager musicManager = getGuildAudioPlayer(message.getGuild());
        musicManager.getScheduler().clear();
        return simpleMessageBuilder("Info", "Queue has been cleared");
    }

    public MessageEmbed shuffle(Message message){
        if(isNotConnected(message.getGuild()))
            return botNotConnected();
        if(!(message.getMember().hasPermission(Permission.KICK_MEMBERS)||isInSameChannel(message.getMember()))) {
            return sendSameChannel();
        }
        GuildMusicManager musicManager = getGuildAudioPlayer(message.getGuild());
        musicManager.getScheduler().shuffle();
        return simpleMessageBuilder("Info", "Queue has been shuffled");
    }

    private void play(Guild guild, GuildMusicManager musicManager, AudioTrack track, Member member) {
        if(!guild.getAudioManager().isConnected() && member.getVoiceState()!=null)
            guild.getAudioManager().openAudioConnection(member.getVoiceState().getChannel());
        musicManager.getScheduler().queue(track);
    }

    /**
     * Checks if the Bot has an audio connection
     * @param guild The Server where it has it's connection
     * @return True if not connected false if connected
     */
    private boolean isNotConnected(Guild guild){
        return !guild.getAudioManager().isConnected();
    }

    public static PlayerManager getManager() {
        if(manager==null)
            manager=new PlayerManager();
        return manager;
    }


	public MessageEmbed leave(Message message) {
		if(isNotConnected(message.getGuild()))
            return simpleMessageBuilder("Info", "Bot is not connected");
		if(!(message.getMember().hasPermission(Permission.KICK_MEMBERS)||isInSameChannel(message.getMember()))) {
            return sendSameChannel();
		}
		stop(message);
		message.getGuild().getAudioManager().closeAudioConnection();
		musicManagers.remove(message.getGuild().getIdLong());
        return simpleMessageBuilder(null,"Disconnected");
    }
	
	private boolean isInSameChannel(Member member) {
		if(!member.getVoiceState().inVoiceChannel())//Client not connected
			return false;
		if(isNotConnected(member.getGuild()))//Bot not connected
			return false;
        //Not in same channel
        return member.getVoiceState().getChannel() == member.getGuild().getAudioManager().getConnectedChannel();
    }
	
	private MessageEmbed sendSameChannel() {
        return simpleMessageBuilder("Warning", "You have to be in the same channel as the Bot");
	}

	private MessageEmbed botNotConnected(){
        return simpleMessageBuilder("Info", "Bot is not connected");
    }

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
