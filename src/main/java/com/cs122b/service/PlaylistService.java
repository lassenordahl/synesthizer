package com.cs122b.service;

import com.cs122b.client.Query;
import com.cs122b.client.SQLClient;
import com.cs122b.model.Playlist;
import com.cs122b.model.Track;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistService {

    private static SQLClient db;

    private static void setPlaylistAttrs(Playlist playlist, ResultSet result) throws SQLException {
        playlist.setId(result.getInt("id"));
        playlist.setName(result.getString("name"));
        playlist.setImage(result.getString("image"));
        playlist.setCreation_date(result.getString("creation_date"));
    }

    private static void insertPlaylist(Playlist playlist, int userId) throws SQLException {
        db = new SQLClient();

        String insertQuery = "INSERT INTO playlist(id, name, image, creation_date) " + "VALUES(DEFAULT,?,?,DEFAULT);";
        PreparedStatement pstmt = db.getConnection().prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, playlist.getName());
        pstmt.setString(2, playlist.getImage());
        int affectedRows = pstmt.executeUpdate();

        if (affectedRows > 0) {
            // get the ID back
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                playlist.setId(rs.getInt(1));
            }
        }

        pstmt.close();

        String insertRelationQuery = "INSERT INTO playlist_to_user(user_id, playlist_id) " + "VALUES(?,?);";
        PreparedStatement relationPstmt = db.getConnection().prepareStatement(insertRelationQuery,
                Statement.RETURN_GENERATED_KEYS);
        relationPstmt.setInt(1, userId);
        relationPstmt.setInt(2, playlist.getId());
        relationPstmt.executeUpdate();

        String insertTrackPlaylistQuery = "INSERT INTO track_in_playlist(playlist_id, track_id) VALUES ";
        for (int i = 0; i < playlist.getTracks().size(); i++)
            insertTrackPlaylistQuery += "(" + playlist.getId() + ", \"" + playlist.getTracks().get(i).getId() + "\")" + ((i < playlist.getTracks().size() - 1) ? "," : "");
        PreparedStatement insertTracksStatement = db.getConnection().prepareStatement(insertTrackPlaylistQuery,
                Statement.RETURN_GENERATED_KEYS);
        insertTracksStatement.executeUpdate();

        relationPstmt.close();
    }

    public static Playlist createPlaylist(JsonObject playlistJson, int userId) throws SQLException {
        db = new SQLClient();

        String name = playlistJson.get("name").getAsString();
        // Check if exists (return null if exists)
        Query query = db.query(String.format(
                "SELECT *  FROM playlist NATURAL JOIN playlist_to_user WHERE id = playlist_id AND name='%s' AND user_id='%d' ",
                name, userId));
        ResultSet result = query.getResult();
        if (result.next() != false) {
            return null;
        }

        query.closeQuery();

        // Create playlist
        Playlist playlist = new Playlist();

        playlist.setName(name);
        playlist.setImage(playlistJson.get("image").getAsString());
        JsonArray tracks = playlistJson.getAsJsonArray("tracks");

        for (int i = 0; i < tracks.size(); i++) {
            JsonObject track = (JsonObject) tracks.get(i);
            Track newTrack = new Track();
            newTrack.setId(track.get("id").getAsString());
            playlist.addTrack(newTrack);
        }

        insertPlaylist(playlist, userId);

        db.closeConnection();
        return playlist;
    }

    public static Playlist fetchPlaylist(int id, int userId) throws SQLException {
        db = new SQLClient();

        Query query = db.query(String.format(
                "SELECT *  FROM playlist NATURAL JOIN playlist_to_user WHERE id='%d' AND id = playlist_id AND user_id='%d'",
                id, userId));

        Playlist playlist = new Playlist();
        ResultSet result = query.getResult();

        if (result.next() == false) {
            return null;
        }

        setPlaylistAttrs(playlist, result);

        query.closeQuery();
        db.closeConnection();
        return playlist;
    }

    public static List<Playlist> fetchPlaylists(int userId, int offset, int limit) throws SQLException {
        db = new SQLClient();

        Query query = db.query(String.format(
                "SELECT *  FROM playlist NATURAL JOIN playlist_to_user WHERE id = playlist_id AND user_id='%d'",
                userId));

        List<Playlist> playlists = new ArrayList<Playlist>();
        ResultSet result = query.getResult();
        while (result.next()) {
            Playlist playlist = new Playlist();
            setPlaylistAttrs(playlist, result);
            playlists.add(playlist);
        }

        query.closeQuery();
        db.closeConnection();
        return playlists;
    }
}
