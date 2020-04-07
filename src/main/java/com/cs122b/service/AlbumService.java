package com.cs122b.service;

import com.cs122b.client.Config;
import com.cs122b.model.Album;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlbumService implements Config {
    public static List<Album> fetchAlbums(int offset, int limit) throws SQLException {
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
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:" + Config.dbtype + ":///" + Config.dbname + "?autoReconnect=true",
                    Config.username, Config.password); // &useSSL=false
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create an execute an SQL statement to select all of table tracks records
        Statement select = connection.createStatement();
        String query = "SELECT album.id, album.name, album.album_type, album.image, album.release_date, artist.name as artist_name FROM album\n" +
                "LEFT JOIN artist_in_album as a_to_a ON a_to_a.album_id = album.id\n" +
                "LEFT JOIN artist ON a_to_a.artist_id = artist.id LIMIT " + Integer.toString(limit);
        ResultSet result = select.executeQuery(query);

        List<Album> albums = new ArrayList<Album>();

        while (result.next()) {
            Album album = new Album(result.getString("id"),
                    result.getString("name"),
                    result.getString("album_type"),
                    result.getString("image"),
                    result.getString("release_date"),
                    result.getString("artist_name"));
            albums.add(album);
        }

        select.close();
        result.close();
        connection.close();

        return albums;
    }

    public static Album fetchAlbum(String id) throws SQLException {
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
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:" + Config.dbtype + ":///" + Config.dbname + "?autoReconnect=true",
                    Config.username, Config.password); // &useSSL=false
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create an execute an SQL statement to select all of table tracks records
        Statement select = connection.createStatement();
        String query = "SELECT album.id, album.name, album.album_type, album.image, album.release_date, artist.name as artist_name FROM album\n" +
                "LEFT JOIN artist_in_album as a_to_a ON a_to_a.album_id = album.id\n" +
                "LEFT JOIN artist ON a_to_a.artist_id = artist.id \n" +
                "WHERE album.id = \"" + id + "\"";
        ResultSet result = select.executeQuery(query);

        result.next();

        Album album = new Album(result.getString("id"),
                result.getString("name"),
                result.getString("album_type"),
                result.getString("image"),
                result.getString("release_date"),
                result.getString("artist_name"));

        select.close();
        result.close();
        connection.close();

        return album;
    }
}
