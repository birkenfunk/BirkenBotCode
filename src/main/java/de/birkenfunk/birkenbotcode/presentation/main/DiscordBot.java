package de.birkenfunk.birkenbotcode.presentation.main;

import de.birkenfunk.birkenbotcode.infrastructure.reader.ReadFile;
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

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Main class which starts the Discord Bot
 * @author Alexander Asbeck
 * @version 1.4
 */
public class DiscordBot {
	private static final Logger LOGGER = LogManager.getLogger(DiscordBot.class);
	private final JDA shardman;
	private static DiscordBot discordBot;
	private final Shutdown s1;

	public static void main(String[] args)  {
		try {
			discordBot= new DiscordBot();
		} catch (LoginException | IllegalArgumentException e) {
			e.printStackTrace();
		}

	}
	
	public DiscordBot() throws LoginException, IllegalArgumentException{
		ReadFile readFile= ReadFile.getReadFile();
		JDABuilder builder = JDABuilder.createDefault(readFile.getToken());
		builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
		builder.setMemberCachePolicy(MemberCachePolicy.ALL);
		builder.setStatus(OnlineStatus.ONLINE);
		builder.setActivity(Activity.watching(readFile.getStatus()));
		builder.addEventListeners(new MessageListener(),new ReactionListener(), new EventListener());
		shardman= builder.build();
		LOGGER.info("Bot online");

		s1=new Shutdown();
		Thread t1= new Thread(s1);
		t1.start();
	}

	/**
	 * Class listens to messages via command line
	 * to shut down the Bot
	 * @author Alexander Asbeck
	 * @version 2.0
	 */
	class Shutdown implements Runnable{

		@Override
		public void run() {
			String line;

			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			try {
				while ((line=reader.readLine())!=null) {

					if(line.equalsIgnoreCase("exit"))
					{
						shardman.shutdown();
						LOGGER.info("Bot offline");
						reader.close();
						System.exit(0);
					}else {
						LOGGER.info("Use 'exit' to shutdown");
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * To stop the Thread from outside and also stop the Bot
		 */
		public void stop(){
			LOGGER.info("Bot offline");
			System.exit(0);
		}
	}

	/**
	 * Returns an instance of the Bot
	 * @return instance of Bot
	 */
	public static DiscordBot getDiscordBot() {
		return discordBot;
	}

	/**
	 * Stops the Thread
	 */
	public void stopThreadListener(){
		s1.stop();
	}
}