package de.birkenfunk.birkenbotcode.presentation.audio_player;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
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

    private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
            musicManager.getPlayer().setVolume(50);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    public void loadAndPlay(Message message, String trackUrl){
        TextChannel channel = message.getTextChannel();
        Guild guild = message.getGuild();
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
        if(guild.getAudioManager().isConnected() && guild.getAudioManager().getConnectedChannel()!= Objects.requireNonNull(Objects.requireNonNull(message.getMember()).getVoiceState()).getChannel()){
            channel.sendMessage("You are not in the Same Channel as the Bot");
            return;
        }
        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                channel.sendMessage("Adding to queue: " + track.getInfo().title).queue();

                play(guild, musicManager, track, message.getMember());
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();
                if(firstTrack == null)
                    firstTrack = playlist.getTracks().get(0);

                channel.sendMessage("Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")").queue();
                
                play(guild, musicManager, firstTrack, message.getMember());
            }

            @Override
            public void noMatches() {
                channel.sendMessage("Nothing found by " + trackUrl).queue();
            }

            @Override
            public void loadFailed(FriendlyException e) {
                channel.sendMessage("Could not play: " + e.getMessage()).queue();
            }
        });
    }

    public void skipTrack(Message message){
        if(isNotConnected(message))
            return;
        if(message.getGuild().getAudioManager().getConnectedChannel()!=message.getMember().getVoiceState().getChannel()){
            message.getChannel().sendMessage("You have to be in the same Voice Channel as the Bot").queue();
        }
        GuildMusicManager musicManager = getGuildAudioPlayer(message.getGuild());
        musicManager.getScheduler().nextTrack();
        message.getChannel().sendMessage("Skipped to next track.").queue();
    }

    /**
     *
     * @param message
     */
    public void pauseTrack(Message message){
        if(isNotConnected(message))
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
    public void UnPauseTrack(Message message){
        if(isNotConnected(message))
            return;
        if(message.getGuild().getAudioManager().getConnectedChannel()!=message.getMember().getVoiceState().getChannel()){
            message.getChannel().sendMessage("You have to be in the same Voice Channel as the Bot").queue();
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
    public void Repeat(Message message, Boolean repeat1){
        if(isNotConnected(message))
            return;
        if(message.getGuild().getAudioManager().getConnectedChannel()!=message.getMember().getVoiceState().getChannel()){
            message.getChannel().sendMessage("You have to be in the same Voice Channel as the Bot").queue();
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

    public void Volume(int volume, Message message){
        if(isNotConnected(message))
            return;
        if(message.getGuild().getAudioManager().getConnectedChannel()!=message.getMember().getVoiceState().getChannel()){
            message.getChannel().sendMessage("You have to be in the same Voice Channel as the Bot").queue();
        }
        GuildMusicManager musicManager = getGuildAudioPlayer(message.getGuild());
        musicManager.getPlayer().setVolume(volume);
        message.getChannel().sendMessage("Set Volume to "+volume+"%").queue();
    }

    public void Stop(Message message){
        GuildMusicManager musicManager = getGuildAudioPlayer(message.getGuild());
        musicManager.getScheduler().stop();
    }

    public void Clear(Message message){
        GuildMusicManager musicManager = getGuildAudioPlayer(message.getGuild());
        musicManager.getScheduler().clear();
        message.getChannel().sendMessage("Queue has been cleared").queue();
    }

    public void Shuffle(Message message){
        GuildMusicManager musicManager = getGuildAudioPlayer(message.getGuild());
        musicManager.getScheduler().shuffle();
        message.getChannel().sendMessage("Queue has been shuffled").queue();
    }

    private void play(Guild guild, GuildMusicManager musicManager, AudioTrack track, Member member) {
        if(!guild.getAudioManager().isConnected())
            guild.getAudioManager().openAudioConnection(member.getVoiceState().getChannel());
        musicManager.getScheduler().queue(track);
    }

    private boolean isNotConnected(Message message){
        if(!message.getGuild().getAudioManager().isConnected())
            message.getChannel().sendMessage("Bot is not Connected");
        return !message.getGuild().getAudioManager().isConnected();
    }

    public static PlayerManager getManager() {
        if(manager==null)
            manager=new PlayerManager();
        return manager;
    }
}
