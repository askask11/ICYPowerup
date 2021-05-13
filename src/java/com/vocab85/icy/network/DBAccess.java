/*
 * Author: jianqing
 * Date: May 9, 2021
 * Description: This document is created for
 */
package com.vocab85.icy.network;

import cn.hutool.setting.Setting;
import com.vocab85.icy.model.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * The class to access database.
 * @author Jianqing Gao
 */

public class DBAccess implements AutoCloseable
{
    private Connection dbConn;
    
     
    public User getUser(String username, String password) throws SQLException
    {
        User user;
        PreparedStatement ps = dbConn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if(rs.next())
        {
            user = new User();
            user.setId(rs.getInt("id"));
            user.setIcyid(rs.getString("icyid"));
            user.setToken(rs.getString("token"));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            
            return user;
        }
        return null;
    }
    
    public int deregisterUserIcy(int userid)throws SQLException
    {
        Statement s = dbConn.createStatement();
        return s.executeUpdate("UPDATE users SET icyid=NULL WHERE id="+userid);
    }
    
    
    public int updateUserId(int userPowerUpId, String userIcyId) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("UPDATE users SET icyid=? WHERE users.id=?");
        ps.setString(1, userIcyId);
        ps.setInt(2, userPowerUpId);
        return ps.executeUpdate();
    }
    
    
    
     
    public DBAccess()
    {
        dbConn = null;
        
        
    }
    
    
    public void connect(String url, String dbUsername, String dbPassword, boolean useSSL) throws SQLException
    {
        //String connectionURL = "jdbc:mysql://" + host + "/" + dbName;
        this.dbConn = null;

        //URL for new version jdbc connector.
        Properties properties = new Properties(); //connection system property
        properties.setProperty("user", dbUsername);
        properties.setProperty("password", dbPassword);
        properties.setProperty("useSSL", Boolean.toString(useSSL));//set this true if domain suppotes SSL
        //"-u root -p mysql1 -useSSL false"
        this.dbConn = DriverManager.getConnection(url, properties);
    }

    public static DBAccess getDefaultInstance() throws SQLException
    {
        DBAccess d = new DBAccess();
        Setting s = new Setting("db.setting");
        d.connect(s.get("url"), s.get("user"), s.get("pass"), true);
        return d;
    }
    
    
    @Override
    public void close() throws SQLException
    {
        this.dbConn.close();
    }
    public static void main(String[] args) throws SQLException
    {
        DBAccess d= getDefaultInstance();
        System.out.println(d.getUser("jgao", "e"));
    }
}
