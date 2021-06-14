package de.birkenfunk.birkenbotcode.presentation.main;

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
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Main class which starts the Discord Bot
 * @author Alexander Asbeck
 * @version 1.4
 */
@SpringBootApplication(scanBasePackages = {"de.birkenfunk.birkenbotcode"})
public class DiscordBot {
	private static final Logger LOGGER = LogManager.getLogger(DiscordBot.class);
	private JDA shardman;
	private Shutdown s1;

	@Autowired
	private IDatabase database;

	public static void main(String[] args)  {
		SpringApplication.run(DiscordBot.class, args);
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
	 * Stops the Thread
	 */
	public void stopThreadListener(){
		s1.stop();
	}
}
