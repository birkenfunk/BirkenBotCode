package de.birkenfunk.birkenbot;

import java.util.ResourceBundle;

/**
 * Reads the configuration from the config.properties file.
 * @Author Birkenfunk
 */
public class ConfigReader {
    private static final String CONFIG_FILE = "config.properties";
    private static final ResourceBundle CONFIG = ResourceBundle.getBundle(CONFIG_FILE);
    /**
     * Returns the UserIDs which are allowed to administrate the bot.
     * Takes the value from the config file.
     */
    public static String[] getAdminUserIDs() {
        return CONFIG.getString("adminUserIDs").split(",");
    }
}
