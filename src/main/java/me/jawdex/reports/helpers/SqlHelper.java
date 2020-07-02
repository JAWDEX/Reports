package me.jawdex.reports.helpers;

import org.bukkit.Bukkit;

import java.sql.*;

public class SqlHelper {
    private String ip;
    private String port;
    private String database;
    private String username;
    private String password;
    private Connection conn;

    public SqlHelper(String ip, String port, String database, String username, String password) {
        this.ip = ip;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public synchronized boolean connect() {
        try {
            disconnect();
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + ip + ":" + port + '/' + database + "?allowMultiQueries=true&autoReconnect=true", username, password);
            conn.prepareStatement("CREATE TABLE IF NOT EXISTS `DiscordMessages` (type VARCHAR(16), sender VARCHAR(16), target VARCHAR(16), message VARCHAR(512));").executeUpdate();
            return true;
        } catch (SQLException e) {
            conn = null;
            e.printStackTrace();
            return false;
        }
    }

    public synchronized void disconnect() {
        if (conn == null) {
            System.out.println("There is no connection to close!");
            return;
        }
        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void refreshConnection() {
        try {
            PreparedStatement s = conn.prepareStatement("SELECT 1");
            s.executeQuery();
        }
        catch (SQLException e) {
            connect();
        }
    }

    public synchronized boolean isConnected() {
        try {
            return (conn != null) && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public synchronized Connection getConnection() {
        return conn;
    }

    public synchronized void runUpdate(String query) {
        if (!isConnected()) {
            System.out.println("You need to open the connection to the database BEFORE you execute a query.");
            return;
        }

        try {
            Statement s = conn.createStatement();
            s.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized ResultSet runQuery(String query) {
        if (!isConnected()) {
            Bukkit.getConsoleSender()
                    .sendMessage("You need to open the connection to the database BEFORE you execute a query.");
            return null;
        }

        try {
            Statement s = conn.createStatement();
            return s.executeQuery(query);
        } catch (Exception e) {
            return null;
        }
    }
}
