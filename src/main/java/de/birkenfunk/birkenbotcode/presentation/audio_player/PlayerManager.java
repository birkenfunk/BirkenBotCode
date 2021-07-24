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
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite.Channel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
            musicManager.getPlayer().setVolume(50);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    /**
     * Adds a new track to the queue
     * @param channel
     * @param member
     * @param trackUrl
     */
    public void loadAndPlay(Message message){
        Guild guild = message.getGuild();
        Member member = message.getMember();
        TextChannel channel = message.getChannel();
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
        
        if(!member.getVoiceState().inVoiceChannel()) {//if not in a channel
        	channel.sendMessage(member.getAsMention() + " you have to be in a voice channel").queue();
        	return;
        }
        
        if(!isInSameChannel(message.getMember())&&!isNotConnected(guild)) {//already connected but not in same channel
			sendSameChannel(channel);
			return;
		}
        
        playerManager.loadItemOrdered(musicManager, message.getContent(), new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                channel.sendMessage("Adding to queue: " + track.getInfo().title).queue();

                play(guild, musicManager, track, member);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();
                if(firstTrack == null)
                    firstTrack = playlist.getTracks().get(0);

                channel.sendMessage("Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")").queue();
                
                play(guild, musicManager, firstTrack, member);
            }

            @Override
            public void noMatches() {
                channel.sendMessage("Nothing found by " + message.getContent()).queue();
            }

            @Override
            public void loadFailed(FriendlyException e) {
                channel.sendMessage("Could not play: " + e.getMessage()).queue();
            }
        });
    }

    public void skipTrack(Message message){
        if(isNotConnected(message.getGuild()))
            return;
        if(message.getGuild().getAudioManager().getConnectedChannel()!=message.getMember().getVoiceState().getChannel()){
        	message.getChannel().sendMessage("You have to be in the same Voice Channel as the Bot").queue();
        }
        GuildMusicManager musicManager = getGuildAudioPlayer(message.getChannel().getGuild());
        musicManager.getScheduler().nextTrack();
        message.getChannel().sendMessage("Skipped to next track.").queue();
    }

    /**
     *
     * @param message
     */
    public void pauseTrack(Message message){
        if(isNotConnected(message.getGuild()))
            return;
        if(message.getGuild().getAudioManager().getConnectedChannel()!=message.getMember().getVoiceState().getChannel()){
            message.getChannel().sendMessage("You have to be in the same Voice Channel as the Bot").queue();
        }
        GuildMusicManager musicManager = getGuildAudioPlayer(message.getGuild());
        if(musicManager.getPlayer().isPaused()){
            musicManager.getPlayer().setPaused(false);
            message.getChannel().sendMessage("Unpaused Player").queue();
        }else {
            musicManager.getPlayer().setPaused(true);
            message.getChannel().sendMessage("Paused Player").queue();
        }
    }

    /**
     *
     * @param message
     */
    public void play(Message message){
        if(message.getContent()!=null)
            loadAndPlay(message);
        if(!(message.getMember().hasPermission(Permission.KICK_MEMBERS)||isInSameChannel(message.getMember()))) {
			sendSameChannel(message.getChannel());
			return;
		}
        GuildMusicManager musicManager = getGuildAudioPlayer(message.getGuild());
        musicManager.getPlayer().setPaused(false);
        message.getChannel().sendMessage("Unpaused Player").queue();
    }

    /**
     *
     * @param message
     * @param repeat1
     */
    public void repeat(Message message, Boolean repeat1){
        if(isNotConnected(message.getGuild()))
            return;
        if(!(message.getMember().hasPermission(Permission.KICK_MEMBERS)||isInSameChannel(message.getMember()))) {
			sendSameChannel(message.getChannel());
			return;
		}
        GuildMusicManager musicManager = getGuildAudioPlayer(message.getGuild());
        TrackScheduler trackScheduler = musicManager.getScheduler();
        if (repeat1){
            if(trackScheduler.isRepeat1()){
                trackScheduler.setRepeat1(false);
                message.getChannel().sendMessage("Song isn't in loop any more").queue();
            }else {
                trackScheduler.setRepeat1(true);
                message.getChannel().sendMessage("Song is in loop").queue();
            }

        }else {
            if(trackScheduler.isRepeat()){
                trackScheduler.setRepeat(false);
                message.getChannel().sendMessage("Playlist isn't in loop any more").queue();
            }else {
                trackScheduler.setRepeat(true);
                message.getChannel().sendMessage("Playlist is in loop").queue();
            }
        }
    }

    public void volume(int volume, Message message){
        if(isNotConnected(message.getGuild()))
            return;
        if(!(message.getMember().hasPermission(Permission.KICK_MEMBERS)||isInSameChannel(message.getMember()))) {
			sendSameChannel(message.getChannel());
			return;
		}
        GuildMusicManager musicManager = getGuildAudioPlayer(message.getGuild());
        musicManager.getPlayer().setVolume(volume);
        message.getChannel().sendMessage("Set Volume to "+volume+"%").queue();
    }

    public void stop(Message message){
    	if(isNotConnected(message.getGuild()))
            return;
    	if(!(message.getMember().hasPermission(Permission.KICK_MEMBERS)||isInSameChannel(message.getMember()))) {
			sendSameChannel(message.getChannel());
			return;
		}
        GuildMusicManager musicManager = getGuildAudioPlayer(message.getGuild());
        musicManager.getScheduler().stop();
    }

    public void clear(Message message){
    	if(isNotConnected(message.getGuild()))
            return;
    	if(!(message.getMember().hasPermission(Permission.KICK_MEMBERS)||isInSameChannel(message.getMember()))) {
			sendSameChannel(message.getChannel());
			return;
		}
        GuildMusicManager musicManager = getGuildAudioPlayer(message.getGuild());
        musicManager.getScheduler().clear();
        message.getChannel().sendMessage("Queue has been cleared").queue();
    }

    public void shuffle(Message message){
        GuildMusicManager musicManager = getGuildAudioPlayer(message.getGuild());
        musicManager.getScheduler().shuffle();
        message.getChannel().sendMessage("Queue has been shuffled").queue();
    }

    private void play(Guild guild, GuildMusicManager musicManager, AudioTrack track, Member member) {
        if(!guild.getAudioManager().isConnected())
            guild.getAudioManager().openAudioConnection(member.getVoiceState().getChannel());
        musicManager.getScheduler().queue(track);
    }

    private boolean isNotConnected(Guild guild){
        return !guild.getAudioManager().isConnected();
    }

    public static PlayerManager getManager() {
        if(manager==null)
            manager=new PlayerManager();
        return manager;
    }


	public void leave(Message message) {
		if(isNotConnected(message.getGuild()))
            return;
		if(!(message.getMember().hasPermission(Permission.KICK_MEMBERS)||isInSameChannel(message.getMember()))) {
			sendSameChannel(message.getChannel());
			return;
		}
		stop(message);
		message.getGuild().getAudioManager().closeAudioConnection();
		message.getChannel().sendMessage("Disconnected");
		musicManagers.remove(message.getGuild().getIdLong());
	}
	
	private boolean isInSameChannel(Member member) {
		if(!member.getVoiceState().inVoiceChannel())//Client not connected
			return false;
		if(isNotConnected(member.getGuild()))//Bot not connected
			return false;
		if (member.getVoiceState().getChannel()!=member.getGuild().getAudioManager().getConnectedChannel()) //Not in same channel
			return false;
		return true;
	}
	
	private void sendSameChannel(TextChannel cannel) {
		cannel.sendMessage("You have to be in the same Channel as the Bot").queue();
	}
}
