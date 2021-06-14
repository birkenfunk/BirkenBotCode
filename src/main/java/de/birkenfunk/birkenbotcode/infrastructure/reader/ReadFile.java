package de.birkenfunk.birkenbotcode.infrastructure.reader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * A Class for reading attributes out of a file
 * @author Alexander Asbeck
 * @version 1.0
 */
public class ReadFile {

    private static ReadFile readFile;
    private String username;
    private String password;
    private String token;
    private String database;
    private String status;
    private char prefix;

    /**
     * Creates a new {@link ReadFile}
     */
    private ReadFile(){
        read();
    }

    /**
     * Method for reading out of a file
     * If file doesn't exits it Creates a new one
     */
    private void read(){
        try {
            File file = new File("Login.txt");
            if (!file.exists()) {//Checks if file exists
                fill(file.getName());
                System.exit(0);
            }
            Scanner myReader = new Scanner(file);
            while (myReader.hasNext())
                fillToVar(myReader.nextLine());
            myReader.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Creates a File with specific Information
     * @param file Name of the file
     * @throws IOException if something fails
     */
    private void fill(String file) throws IOException {
        System.out.println("Please Fill the File First");
        FileWriter myWriter = new FileWriter(file);
        myWriter.write("If % in front of Line Spaces won't be removed\n" +
                "Username= \n" +
                "Password= \n" +
                "Database= \n" +
                "Token= \n" +
                "%Status= nach den Mods ;-)\n" +
                "Prefix= ");
        myWriter.close();
    }

    /**
     * Puts the data in the correct variable
     * @param data the data that should be filled in the correct variable
     */
    private void fillToVar(String data) throws IOException {
        if (data.charAt(0)!='%') {
            data = data.replaceAll("\\s+", "");
        }else {
            data = data.replaceAll("%", "");
        }
        String[] strings=data.split("=");
        if(strings.length<1){
            fill("Login.txt");
            System.exit(0);
        }
        if(strings[0].equalsIgnoreCase("Username"))
            username = strings[1];
        if(strings[0].equalsIgnoreCase("Password"))
            password = strings[1];
        if(strings[0].equalsIgnoreCase("Database"))
            database = strings[1];
        if(strings[0].equalsIgnoreCase("Token"))
            token = strings[1];
        if(strings[0].equalsIgnoreCase("Status"))
            status = strings[1];
        if(strings[0].equalsIgnoreCase("Prefix"))
            prefix = strings[1].charAt(0);
    }

    /**
     * Return the Username for the MySQL Database
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Return the Password for the MySQL Database
     * @return the password
     */
    public String getPassword() {
        return password;
    }
    /**
     * Return the token for the Bot
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * Return the Link for the MySQL Database
     * @return the Link
     */
    public String getDatabase() {
        return database;
    }

    /**
     * Return the Status for the Bot
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Return the Prefix for commands the Bot
     * @return the Prefix
     */
    public char getPrefix() {
        return prefix;
    }

    /**
     * Returns the ReadFile if it doesn't exits it will create a new one
     * @return A Instance of the ReadFile
     */
    public static ReadFile getReadFile() {
        if(readFile==null)
            readFile = new ReadFile();
        return readFile;
    }
}
