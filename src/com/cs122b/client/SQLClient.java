package com.cs122b.client;

import java.sql.*;
import java.util.ArrayList;

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
            connection = DriverManager.getConnection(
                    "jdbc:" + Config.dbtype + ":///" + Config.dbname + "?autoReconnect=true", Config.username,
                    Config.password); // &useSSL=false
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Query query(String query) throws SQLException {
        return new Query(connection.createStatement(), query);
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    public ArrayList<String> getTables() throws SQLException {
        ArrayList<String> tableNames = new ArrayList<String>();
        DatabaseMetaData md = connection.getMetaData();
        ResultSet result = md.getTables(null, null, "%", null);
        while (result.next()) {
            tableNames.add(result.getString("TABLE_NAME"));
        }
        return tableNames;
    }

    public ArrayList<String> getColumns(String tableName) throws SQLException {
        ArrayList<String> attributes = new ArrayList<String>();
        DatabaseMetaData md = connection.getMetaData();
        ResultSet result = md.getColumns(null, null, tableName, null);
        while (result.next()) {
            attributes.add(result.getString("COLUMN_NAME") + " " + result.getString("TYPE_NAME"));
        }
        return attributes;
    }
}
