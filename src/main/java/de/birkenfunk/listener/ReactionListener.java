package de.birkenfunk.listener;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

/**
 * Class for reacts to a message
 * @author Alexander Asbeck
 * @version 1.2
 */
public class ReactionListener extends ListenerAdapter {

    /*todo Documentation*/

    private HashMap<String,Long> Role; //For the Role somebody should get

    public ReactionListener() {
        super();
        fill();
    }

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();
        String emote =event.getReactionEmote().getName();
        //guild.removeRoleFromMember(event.getMember(),guild.getRoleById(414752929795276800L)).queue();
        //event.getChannel().addReactionById(event.getMessageIdLong(),event.getReactionEmote().getEmote()).queue();
        if(event!=null && event.getMessageIdLong()== 574549689290981377L && member!=null && guild != null){
            guild.addRoleToMember(member, Objects.requireNonNull(guild.getRoleById(Role.get(emote)))).queue();
        }
    }

    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();
        String emote =event.getReactionEmote().getName();
        if(event!=null && event.getMessageIdLong()== 574549689290981377L && member!=null && guild != null){
            guild.removeRoleFromMember(member, Objects.requireNonNull(guild.getRoleById(Role.get(emote)))).queue();
        }
    }

    private void fill(){
        Role=new HashMap<>();
        Role.put("R_",414752929795276800L);
        Role.put("P_",461963203614605313L);
        Role.put("B_",415883643374927874L);
        Role.put("D_",462696107051319298L);
        Role.put("W_",526541891672473605L);
        Role.put("G_",614908719964356631L);
        Role.put("E_",715644497513545749L);
        Role.put("L_",726396735298994186L);
        Role.put("A_",762397925724127243L);
        Role.put("S_",792840051498942475L);
    }
}
