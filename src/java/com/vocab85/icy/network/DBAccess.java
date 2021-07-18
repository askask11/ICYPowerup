/*
 * Author: jianqing
 * Date: May 9, 2021
 * Description: This document is created for
 */
package com.vocab85.icy.network;

import cn.hutool.setting.Setting;
import com.vocab85.icy.model.ICYPostcard;
import com.vocab85.icy.model.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

/**
 * The class to access database.
 *
 * @author Jianqing Gao
 */
public class DBAccess implements AutoCloseable
{

    private Connection dbConn;

    public int updateToken(int id, String token) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("UPDATE users SET token=? WHERE users.id=?");
        ps.setString(1, token);
        ps.setInt(2, id);
        return ps.executeUpdate();
    }

    public int updateUsername(int id, String username) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("UPDATE users SET username=? WHERE users.id=?");
        ps.setString(1, username);
        ps.setInt(2, id);
        return ps.executeUpdate();
    }

    public int updatePassword(int id, String password) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("UPDATE users SET password=? WHERE users.id=?");
        ps.setString(1, password);
        ps.setInt(2, id);
        return ps.executeUpdate();
    }

    public int updatePasswordByEmail(String email, String password) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("UPDATE users SET password=? WHERE email=?");
        ps.setString(1, password);
        ps.setString(2, email);
        return ps.executeUpdate();
    }

    public int updateEmail(int id, String email) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("UPDATE users SET email=? WHERE users.id=?");
        ps.setString(1, email);
        ps.setInt(2, id);
        return ps.executeUpdate();
    }

    public boolean isUserExists(String email) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("SELECT COUNT(*) FROM users WHERE email=?");
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;
    }

    public int addUser(int id, String username, String password, String email) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("INSERT INTO users VALUES (?,?,?,?,?,?,?)");
        ps.setInt(1, id);
        ps.setString(2, username);
        ps.setString(3, password);
        ps.setString(4, email);
        ps.setNull(5, java.sql.Types.VARCHAR);
        ps.setNull(6, java.sql.Types.VARCHAR);
        ps.setBoolean(7, false);
        return ps.executeUpdate();
    }

    private User getUserFromRS(ResultSet rs) throws SQLException
    {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setIcyid(rs.getString("icyid"));
        user.setToken(rs.getString("token"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));

        return user;
    }

    public User getUserByEmail(String email, String password) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("SELECT * FROM users WHERE email=? AND password=?");
        ps.setString(1, email);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
        {
            return getUserFromRS(rs);
        }
        return null;
    }

    public int setUserToken(int id, String token) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("UPDATE users SET token=? WHERE users.id=?");
        ps.setString(1, token);
        ps.setInt(2, id);
        return ps.executeUpdate();
    }

    public int clearUserToken(int id) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("UPDATE users SET token=? WHERE users.id=?");
        ps.setNull(1, java.sql.Types.VARCHAR);
        ps.setInt(2, id);
        return ps.executeUpdate();
    }

    public User getUserByToken(String token) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("SELECT * FROM users WHERE token=?");
        ps.setString(1, token);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
        {
            return getUserFromRS(rs);
        }
        return null;
    }

    public User getUser(String username, String password) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
        {
            return getUserFromRS(rs);
        }
        return null;
    }

    public String getCardSeqByCardId(String cardId) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("SELECT cardSeq FROM icyCards WHERE cardId=?");
        ps.setString(1, cardId);
        ResultSet rs = ps.executeQuery();
        if(rs.next())
        {
            return rs.getString(1);
        }
        return null;
    }
    
    public int deregisterUserIcy(int userid) throws SQLException
    {
        Statement s = dbConn.createStatement();
        return s.executeUpdate("UPDATE users SET icyid=NULL WHERE id=" + userid);
    }

    public int updateUserId(int userPowerUpId, String userIcyId) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("UPDATE users SET icyid=? WHERE users.id=?");
        ps.setString(1, userIcyId);
        ps.setInt(2, userPowerUpId);
        return ps.executeUpdate();
    }

    
    public User getUserByUserId(int powerUpId) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("SELECT * FROM users WHERE users.id=?");
        ps.setInt(1, powerUpId);
        ResultSet rs = ps.executeQuery();
        if(rs.next())
        {
            User user = new User();
            user.setId(rs.getInt(1));
            user.setUsername(rs.getString(2));
            user.setEmail(rs.getString(4));
            user.setIcyid(rs.getString(5));
            user.setToken(rs.getString(6));
            return user;
        }
        return null;
    }
    
    public String getIcyIdByUserId(int powerUpId) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("SELECT icyid FROM users WHERE users.id=?");
        ResultSet rs = null;
        ps.setInt(1, powerUpId);
        rs = ps.executeQuery();
        if (rs.next())
        {
            return rs.getString(1);
        }
        return null;
    }

    public boolean isUserHacks(String un, String pass) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("SELECT hacks FROM users WHERE username=? AND password=?");
        ps.setString(1, un);
        ps.setString(2, pass);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
        {
            return rs.getBoolean(1);

        }
        return false;
    }

    public int insertHackR(String username, String pcid, String friend, String carddata) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("INSERT INTO hackcard VALUES(?,?,?,?)");
        ps.setString(1, username);
        ps.setString(2, pcid);
        ps.setString(3, friend);
        ps.setString(4, carddata);
        return ps.executeUpdate();

    }

    public int[] batchInsertCards(ArrayList<ICYPostcard> cardList) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("INSERT INTO icyCards VALUES(?,?,?,?)");
        for (ICYPostcard card : cardList)
        {
            ps.setInt(1, card.getSequence());
            ps.setString(2, card.getCardId());
            ps.setInt(3, card.getSenderId());
            ps.setInt(4, card.getReceiverId());
            ps.addBatch();
        }
        return ps.executeBatch();
    }

    public int getLatestCrawledCardSeqId() throws SQLException
    {
        Statement s = dbConn.createStatement();
        ResultSet rs = s.executeQuery("SELECT `cardSeq` FROM `icyCards` ORDER BY `cardSeq` DESC LIMIT 1");
        rs.next();
        return rs.getInt(1);
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
        DBAccess d = getDefaultInstance();
        System.out.println(d.getUser("jgao", "e"));
    }
}
