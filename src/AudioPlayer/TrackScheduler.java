package AudioPlayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final List<AudioTrack> queue;
    private AudioTrack currentTrack;
    private boolean repeat;
    private boolean repeat1;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedList<>();
    }

    public void queue(AudioTrack track){
        if(!player.startTrack(track,true)) {
            queue.add(track);
        }else {
            currentTrack= track;
        }
    }

    public void nextTrack(){
        if(repeat&&!repeat1){
            queue.add(currentTrack.makeClone());
        }
        currentTrack=queue.remove(0);
        player.startTrack(currentTrack,false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if(endReason.mayStartNext){
            if(repeat1){
                player.startTrack(currentTrack.makeClone(),false);
            }else {
                if(queue.isEmpty()){
                    stop();
                }
                nextTrack();
            }
        }
    }

    void clear(){
        queue.clear();
    }

    void stop(){
        queue.clear();
        player.stopTrack();
    }

    void shuffle(){
        Collections.shuffle(queue);
    }

    boolean isRepeat() {
        return repeat;
    }

    void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    boolean isRepeat1() {
        return repeat1;
    }

    void setRepeat1(boolean repeat1) {
        this.repeat1 = repeat1;
    }
}
