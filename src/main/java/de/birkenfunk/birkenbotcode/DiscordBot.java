package de.birkenfunk.birkenbotcode;

import de.birkenfunk.birkenbotcode.application.IDatabase;
import de.birkenfunk.birkenbotcode.common.Config;
import de.birkenfunk.birkenbotcode.presentation.listener.EventListener;
import de.birkenfunk.birkenbotcode.presentation.listener.MessageListener;
import de.birkenfunk.birkenbotcode.presentation.listener.ReactionListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.security.auth.login.LoginException;
import java.io.IOException;

/**
 * Main class which starts the Discord Bot
 * @author Alexander Asbeck
 * @version 1.4
 */
@SpringBootApplication(scanBasePackages = {"de.birkenfunk.birkenbotcode"})
public class DiscordBot {
	private static final Logger LOGGER = LogManager.getLogger(DiscordBot.class);
	private static JDA shardman;

	@Autowired
	private IDatabase database;

	public static void main(String[] args)  {
		SpringApplication.run(DiscordBot.class, args);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			LOGGER.info("Shutdown");
			shardman.shutdown();
		}));
	}


	@Bean
	void startBot() throws LoginException {
		JDABuilder builder = JDABuilder.createDefault(Config.getConfig().getToken());
		builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
		builder.setMemberCachePolicy(MemberCachePolicy.ALL);
		builder.setStatus(OnlineStatus.ONLINE);
		builder.setActivity(Activity.watching(Config.getConfig().getStatus()));
		builder.addEventListeners(new MessageListener(),new ReactionListener(), new EventListener());
		shardman= builder.build();
		LOGGER.info("Bot online");

	}
}
