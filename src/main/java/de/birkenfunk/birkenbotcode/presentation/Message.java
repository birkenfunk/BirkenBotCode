package de.birkenfunk.birkenbotcode.presentation;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class Message {
	
	private Member member;
	private String content;
	private Guild guild;
	private TextChannel channel;
	public Message(Member member, String content, Guild guild, TextChannel channel) {
		super();
		this.member = member;
		this.content = content;
		this.guild = guild;
		this.channel = channel;
	}
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Guild getGuild() {
		return guild;
	}
	public void setGuild(Guild guild) {
		this.guild = guild;
	}
	public TextChannel getChannel() {
		return channel;
	}
	public void setChannel(TextChannel channel) {
		this.channel = channel;
	}
	
	

}
