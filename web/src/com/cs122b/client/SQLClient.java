package com.cs122b.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class SQLClient {
    private String url;
    private String dbtype;
    private String dbname;
    private String username;
    private String password;
    private Connection connection;

    public SQLClient() throws NamingException, SQLException {

       try (InputStream input = getClass().getResourceAsStream("/config.properties")) {

           Properties prop = new Properties();

           // load a properties file
           prop.load(input);

           this.url = prop.getProperty("db.url");
           this.dbtype = prop.getProperty("db.type");
           this.dbname = prop.getProperty("db.name");
           this.username = prop.getProperty("db.username");
           this.password = prop.getProperty("db.password");

       } catch (IOException ex) {
           System.err.println("Insure that you have config file in src/resources/");
           ex.printStackTrace();
       }

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

        // Connect to the database
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/synesthizer");
            connection = ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public SQLClient(boolean notPooling) {

        try (InputStream input = getClass().getResourceAsStream("/config.properties")) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            this.url = prop.getProperty("db.url");
            this.dbtype = prop.getProperty("db.type");
            this.dbname = prop.getProperty("db.name");
            this.username = prop.getProperty("db.username");
            this.password = prop.getProperty("db.password");

        } catch (IOException ex) {
            System.err.println("Insure that you have config file in src/resources/");
            ex.printStackTrace();
        }

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

        // Connect to the database
        try {
            connection = DriverManager.getConnection(
                    "jdbc:" + this.dbtype + "://" + this.url + "/" + this.dbname + "?autoReconnect=true", this.username,
                    this.password); // &useSSL=false
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Query query(String query) throws SQLException {
        return new Query(connection.createStatement(), query);
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

    public Connection getConnection() throws NamingException, SQLException {
        return this.connection;
    }
}
