package de.birkenfunk.birkenbotcode.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    private static Config config = new Config();
    private String token;
    private String status;

    private Config(){
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream("Bot.properties")){
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        token=properties.getProperty("Token");
        status=properties.getProperty("status");
    }

    public static Config getConfig() {
        return config;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }
}
