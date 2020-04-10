package com.cs122b.client;

import java.sql.*;

public class SQLClient implements Config {
    private Connection connection;

    public SQLClient() {
        // Incorporate mySQL driver
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Connect to the test database
        try {
            connection = DriverManager.getConnection("jdbc:" + Config.dbtype + ":///" + Config.dbname + "?autoReconnect=true",
                    Config.username, Config.password); // &useSSL=false
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public Query query(String query) throws SQLException {
        return new Query(connection.createStatement(), query);
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }
}
