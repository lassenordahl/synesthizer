package com.cs122b.service;

import com.cs122b.Config;
import com.cs122b.model.Track;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrackService implements Config {
    public static List<Track> fetchTracks(int offset, int limit) throws SQLException {
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
            connection = DriverManager.getConnection("jdbc:" + Config.dbtype + ":///" + Config.dbname + "?autoReconnect=true&useSSL=false",
                    Config.username, Config.password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create an execute an SQL statement to select all of table tracks records
        Statement select = connection.createStatement();
        String query = "SELECT * FROM track LIMIT " + Integer.toString(limit);
        ResultSet result = select.executeQuery(query);

        List<Track> tracks = new ArrayList<Track>();

        while (result.next()) {
            Track track = new Track(result.getString("id"),
                                result.getString("name"),
                                result.getInt("track_number"));
            tracks.add(track);
        }


        return tracks;
    }
}
