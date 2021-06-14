package de.birkenfunk.birkenbotcode.persistent;

import java.sql.*;
import java.util.*;

import de.birkenfunk.birkenbotcode.domain.helper_classes.Command;
import de.birkenfunk.birkenbotcode.infrastructure.reader.ReadFile;
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
     * Constructor for MysqlCon
     * Private so it can only be initialised only once
     */
    private MysqlCon() {
        commandIDtoName();
    }

    private void open(){
        try{
            ReadFile readFile=ReadFile.getReadFile();
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://"+readFile.getDatabase()+"?useSSL=false&serverTimezone=UTC",readFile.getUsername(),readFile.getPassword());
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
     * @param userID UserID of a User
     * @param userName Username of a User
     */
    public void writeToMember(Long userID, String userName) throws Exception {
        open();
        if(!running){
            throw(new Exception());
        }
        PreparedStatement preparedStatement;
        try {
            preparedStatement = con.prepareStatement("INSERT INTO UserID_UserName (UserID, Username)"+"values(?,?)");
            preparedStatement.setLong(1,userID);
            preparedStatement.setString(2,userName);
            preparedStatement.execute();
        }catch (SQLIntegrityConstraintViolationException e){ //In case Entrance already exists it Updates it
            try {
                preparedStatement = con.prepareStatement("UPDATE UserID_UserName SET Username = ? where UserID = ?");
                preparedStatement.setLong(2,userID);
                preparedStatement.setString(1,userName);
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
     * @param roleID ID of the Role
     * @param roleName Name of the Role
     */
    public void writeToRole(Long roleID, String roleName) throws Exception {
        open();
        if(!running){
            throw(new Exception());
        }
        PreparedStatement preparedStatement;
        try {
            preparedStatement = con.prepareStatement("INSERT INTO RoleID_RoleName (RoleID, RoleName)"+"values(?,?)");
            preparedStatement.setLong(1,roleID);
            preparedStatement.setString(2,roleName);
            preparedStatement.execute();
        }catch (SQLIntegrityConstraintViolationException e){//In case Entrance already exists it Updates it
            try {
                preparedStatement = con.prepareStatement("UPDATE RoleID_RoleName SET RoleName = ? where RoleID = ?");
                preparedStatement.setLong(2,roleID);
                preparedStatement.setString(1,roleName);
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
     * @param userID ID of User
     * @param roleID ID of corresponding Role
     */
    public void writeUserIDRoleID(Long userID, Long roleID) throws Exception {
        open();
        if(!running){
            throw(new Exception());
        }
        PreparedStatement preparedStatement;
        try {
            preparedStatement = con.prepareStatement("INSERT INTO UserID_RoleID (UserID, RoleID)"+"values(?,?)");
            preparedStatement.setLong(1,userID);
            preparedStatement.setLong(2,roleID);
            preparedStatement.execute();
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        closeCon();
    }

    /**
     * For a Large Amount of Data
     * @param userIDs List of UserIDs
     * @param roleIDs List of RoleIDs
     * @throws Exception if an error occurs during opening
     */
    public void writeUserIDRoleID(List<Long> userIDs, List<Long> roleIDs) throws Exception {
        StringBuilder statement= new StringBuilder();
        statement.append("INSERT INTO UserID_RoleID (UserID, RoleID) values(?,?) ");
        for (int i = 1; i < userIDs.size(); i++) {
            statement.append(",(?,?)");
        }
        open();
        if(!running){
            throw(new Exception());
        }
        PreparedStatement preparedStatement;

            try {
                preparedStatement = con.prepareStatement(statement.toString());
                for (int i = 1; !userIDs.isEmpty()&&!roleIDs.isEmpty(); i+=2) {
                    preparedStatement.setLong(i,userIDs.remove(0));
                    preparedStatement.setLong(i+1,roleIDs.remove(0));
                }

                preparedStatement.execute();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        closeCon();
    }

    /**
     * Removes the User from the Database
     * @param userID Id of the user that should be removed
     */
    public void removeUserFromDB(Long userID) throws Exception {
        open();
        if(!running){
            throw(new Exception());
        }
        PreparedStatement preparedStatement;
        try {
            preparedStatement = con.prepareStatement("DELETE FROM UserID_UserName WHERE USERID = ? ");
            preparedStatement.setLong(1,userID);
            preparedStatement.execute();
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        closeCon();
    }

    /**
     * Removes the Role from the Database
     * @param roleID ID of the Role that should be removed
     */
    public void removeRoleFromDB(Long roleID) throws Exception {
        open();
        if(!running){
            throw(new Exception());
        }
        PreparedStatement preparedStatement;
        try {
            preparedStatement = con.prepareStatement("DELETE FROM RoleID_RoleName WHERE RoleID = ? ");
            preparedStatement.setLong(1,roleID);
            preparedStatement.execute();
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        closeCon();
    }

    /**
     * Removes a User from a specific role
     * @param userID ID of the User
     * @param roleID ID of the Role
     */
    public void removeUserFromRole(Long userID, Long roleID) throws Exception {
        open();
        if(!running){
            throw(new Exception());
        }
        PreparedStatement preparedStatement;
        try {
            preparedStatement = con.prepareStatement("DELETE FROM UserID_RoleID WHERE UserID= ? AND RoleID= ? ");
            preparedStatement.setLong(1,userID);
            preparedStatement.setLong(2,roleID);
            preparedStatement.execute();
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        closeCon();
    }

    private void commandIDtoName(){
        open();
        commandIDtoName = new HashMap<>();
        ResultSet res;
        try {
            res = stmt.executeQuery("SELECT * FROM Commands");
            while (res.next())
            {
                int commandID=res.getInt("CommandID");
                String name=res.getString("Name");
                commandIDtoName.put(name,commandID);
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
                int commandID=res.getInt("CommandID");
                String description=res.getString("Description");
                String name=res.getString("Name");
                boolean serverCommand=res.getBoolean("ServerCommand");
                commands.add(new Command(commandID,description,name,serverCommand));
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        closeCon();
        return commands;
    }
}
