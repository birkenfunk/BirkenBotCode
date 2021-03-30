package de.birkenfunk.MySqlConnection;

import java.sql.*;
import java.util.*;

import de.birkenfunk.HelpClasses.Command;
import de.birkenfunk.Reader.ReadFile;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

/**
 * Class for the Connection to a MySql Database
 * @author Alexander Asbeck
 * @version 2.4
 */
public final class MysqlCon {

    private static final MysqlCon mysqlCon= new MysqlCon();
    private Connection con;
    private Statement stmt;
    private boolean running;
    HashMap<String, Integer> commandIDtoName;

    /**
     * Just for testing purpose
     */
    public static void main(String[] args) {
        try {
            mysqlCon.getCommands();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructor for MysqlCon
     * Private so it can only be initialised only once
     */
    private MysqlCon() {
        CommandIDtoName();
    }

    private void open(){
        try{
            ReadFile readFile=ReadFile.getReadFile();
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://"+readFile.getDatabase()+"?useSSL=false",readFile.getUsername(),readFile.getPassword());
            stmt = con.createStatement();
            running=true;
        }catch (SQLException | ClassNotFoundException e){
            running=false;
            e.printStackTrace();
        }
    }

    /**
     * Return the connection class
     * @return The MySql Connection
     */
    public static MysqlCon getMysqlCon() {
        return mysqlCon;
    }

    /**
     * To Close the connection to the Database
     */
    private void closeCon(){
        try {
            con.close();
            running=false;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * Writes an action to the Database
     * @param command which command has been executed
     * @param user who has executed the command
     */
    public void writeToLog(String command, User user) throws Exception {
        open();
        if(!running){
            throw(new Exception());
        }
        try {
            PreparedStatement preparedStmt = con.prepareStatement("INSERT  INTO log (UserID, CommandID, Time)" +
                    "values (?,?,CURRENT_TIME )");
            preparedStmt.setInt(2,commandIDtoName.get(command));
            if(user!=null) {
                preparedStmt.setLong(1,user.getIdLong());
            }else {
                preparedStmt.setLong(1, 0);
            }
            preparedStmt.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
        closeCon();
    }

    /**
     * Writes the members of a server to the Database
     * @param UserID UserID of a User
     * @param UserName Username of a User
     */
    public void writeToMember(Long UserID, String UserName) throws Exception {
        open();
        if(!running){
            throw(new Exception());
        }
        PreparedStatement preparedStatement;
        try {
            preparedStatement = con.prepareStatement("INSERT INTO UserID_UserName (UserID, Username)"+"values(?,?)");
            preparedStatement.setLong(1,UserID);
            preparedStatement.setString(2,UserName);
            preparedStatement.execute();
        }catch (SQLIntegrityConstraintViolationException e){ //In case Entrance already exists it Updates it
            try {
                preparedStatement = con.prepareStatement("UPDATE UserID_UserName SET Username = ? where UserID = ?");
                preparedStatement.setLong(2,UserID);
                preparedStatement.setString(1,UserName);
                preparedStatement.execute();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        closeCon();
    }

    /**
     * For adding a lot of Users
     * @param members List of Members
     * @throws Exception if an error occurs during opening
     */
    public void writeToMember(List<Member> members) throws Exception{
        StringBuilder statement= new StringBuilder();
        StringBuilder statement2= new StringBuilder();
        Member member;
        statement.append("INSERT INTO UserID_UserName (UserID, Username) values(?,?) ");
        statement2.append("UPDATE UserID_UserName SET Username = ? where UserID = ?; ");
        for (int i = 1; i < members.size(); i++) {
            statement.append(",(?,?)");
            statement2.append("UPDATE UserID_UserName SET Username = ? where UserID = ?; ");
        }
        open();
        if(!running){
            throw(new Exception());
        }
        PreparedStatement preparedStatement;
        try {
            preparedStatement = con.prepareStatement(statement.toString());
            int j = 1;
            for (Member value : members) {
                member = value;
                preparedStatement.setLong(j++, member.getIdLong());
                preparedStatement.setString(j++, member.getUser().getAsTag());
            }
            preparedStatement.execute();
        }catch (SQLIntegrityConstraintViolationException e){ //In case Entrance already exists it Updates it
            try {
                preparedStatement = con.prepareStatement(statement2.toString());
                for (int i = 1; !members.isEmpty(); ) {
                    member=members.remove(0);
                    preparedStatement.setString(i++,member.getUser().getAsTag());
                    preparedStatement.setLong(i++,member.getIdLong());
                }
                preparedStatement.executeBatch();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        closeCon();
    }

    /**
     * Writes the roles of a server to the Database
     * @param RoleID ID of the Role
     * @param RoleName Name of the Role
     */
    public void writeToRole(Long RoleID, String RoleName) throws Exception {
        open();
        if(!running){
            throw(new Exception());
        }
        PreparedStatement preparedStatement;
        try {
            preparedStatement = con.prepareStatement("INSERT INTO RoleID_RoleName (RoleID, RoleName)"+"values(?,?)");
            preparedStatement.setLong(1,RoleID);
            preparedStatement.setString(2,RoleName);
            preparedStatement.execute();
        }catch (SQLIntegrityConstraintViolationException e){//In case Entrance already exists it Updates it
            try {
                preparedStatement = con.prepareStatement("UPDATE RoleID_RoleName SET RoleName = ? where RoleID = ?");
                preparedStatement.setLong(2,RoleID);
                preparedStatement.setString(1,RoleName);
                preparedStatement.execute();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        closeCon();
    }

    /**
     * For adding a big amount of Roles to the List
     * @param roles List of Roles
     * @throws Exception if an error occurs during opening
     */
    public void writeToRole(List<Role> roles) throws Exception {
        StringBuilder statement= new StringBuilder();
        StringBuilder statement2= new StringBuilder();
        Role role;
        statement.append("INSERT INTO RoleID_RoleName (RoleID, RoleName) values (?,?)\n");
        statement2.append("UPDATE RoleID_RoleName SET RoleName = ? where RoleID = ?;\n");
        for (int i = 1; i < roles.size(); i++) {
            statement.append(",(?,?)");
            statement2.append("UPDATE RoleID_RoleName SET RoleName = ? where RoleID = ?;\n");
        }

        open();
        if(!running){
            throw(new Exception());
        }
        PreparedStatement preparedStatement;
            try {
                preparedStatement = con.prepareStatement(statement.toString());
                int j = 1;
                for (Role value : roles) {
                    role = value;
                    preparedStatement.setLong(j++, role.getIdLong());
                    preparedStatement.setString(j++, role.getName());
                }
                preparedStatement.execute();
            } catch (SQLIntegrityConstraintViolationException e) {//In case Entrance already exists it Updates it
                try {
                    preparedStatement = con.prepareStatement(statement2.toString());
                    for (int i = 1; !roles.isEmpty(); ) {
                        role=roles.remove(0);
                        preparedStatement.setString(i++,role.getName());
                        preparedStatement.setLong(i++,role.getIdLong());
                    }
                    preparedStatement.executeBatch();
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        closeCon();
    }

    /**
     * Puts together the RoleID and the UserID
     * @param UserID ID of User
     * @param RoleID ID of corresponding Role
     */
    public void writeUserID_RoleID(Long UserID, Long RoleID) throws Exception {
        open();
        if(!running){
            throw(new Exception());
        }
        PreparedStatement preparedStatement;
        try {
            preparedStatement = con.prepareStatement("INSERT INTO UserID_RoleID (UserID, RoleID)"+"values(?,?)");
            preparedStatement.setLong(1,UserID);
            preparedStatement.setLong(2,RoleID);
            preparedStatement.execute();
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        closeCon();
    }

    /**
     * For a Large Amount of Data
     * @param UserIDs List of UserIDs
     * @param RoleIDs List of RoleIDs
     * @throws Exception if an error occurs during opening
     */
    public void writeUserID_RoleID(List<Long> UserIDs, List<Long> RoleIDs) throws Exception {
        StringBuilder statement= new StringBuilder();
        statement.append("INSERT INTO UserID_RoleID (UserID, RoleID) values(?,?) ");
        for (int i = 1; i < UserIDs.size(); i++) {
            statement.append(",(?,?)");
        }
        open();
        if(!running){
            throw(new Exception());
        }
        PreparedStatement preparedStatement;

            try {
                preparedStatement = con.prepareStatement(statement.toString());
                for (int i = 1; !UserIDs.isEmpty()&&!RoleIDs.isEmpty(); i+=2) {
                    preparedStatement.setLong(i,UserIDs.remove(0));
                    preparedStatement.setLong(i+1,RoleIDs.remove(0));
                }

                preparedStatement.execute();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        closeCon();
    }

    /**
     * Removes the User from the Database
     * @param UserID Id of the user that should be removed
     */
    public void removeUserFromDB(Long UserID) throws Exception {
        open();
        if(!running){
            throw(new Exception());
        }
        PreparedStatement preparedStatement;
        try {
            preparedStatement = con.prepareStatement("DELETE FROM UserID_UserName WHERE USERID = ? ");
            preparedStatement.setLong(1,UserID);
            preparedStatement.execute();
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        closeCon();
    }

    /**
     * Removes the Role from the Database
     * @param RoleID ID of the Role that should be removed
     */
    public void removeRoleFromDB(Long RoleID) throws Exception {
        open();
        if(!running){
            throw(new Exception());
        }
        PreparedStatement preparedStatement;
        try {
            preparedStatement = con.prepareStatement("DELETE FROM RoleID_RoleName WHERE RoleID = ? ");
            preparedStatement.setLong(1,RoleID);
            preparedStatement.execute();
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        closeCon();
    }

    /**
     * Removes a User from a specific role
     * @param UserID ID of the User
     * @param RoleID ID of the Role
     */
    public void removeUserFromRole(Long UserID, Long RoleID) throws Exception {
        open();
        if(!running){
            throw(new Exception());
        }
        PreparedStatement preparedStatement;
        try {
            preparedStatement = con.prepareStatement("DELETE FROM UserID_RoleID WHERE UserID= ? AND RoleID= ? ");
            preparedStatement.setLong(1,UserID);
            preparedStatement.setLong(2,RoleID);
            preparedStatement.execute();
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        closeCon();
    }

    private void CommandIDtoName(){
        open();
        commandIDtoName = new HashMap<>();
        ResultSet res;
        try {
            res = stmt.executeQuery("SELECT * FROM Commands");
            while (res.next())
            {
                int CommandID=res.getInt("CommandID");
                String Name=res.getString("Name");
                commandIDtoName.put(Name,CommandID);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        closeCon();
    }

    /**
     * Returns a list of Commands
     * @return list of {@link Command}
     */
    public List<Command> getCommands(){
        open();
        List<Command> commands = new ArrayList<>();
        try {
            ResultSet res = stmt.executeQuery("SELECT * FROM Commands");
            while (res.next())
            {
                int CommandID=res.getInt("CommandID");
                String Description=res.getString("Description");
                String Name=res.getString("Name");
                boolean ServerCommand=res.getBoolean("ServerCommand");
                commands.add(new Command(CommandID,Description,Name,ServerCommand));
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        closeCon();
        return commands;
    }
}
