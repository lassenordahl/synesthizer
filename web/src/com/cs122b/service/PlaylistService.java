package com.cs122b.service;

import com.cs122b.service.TrackService;
import com.cs122b.client.Query;
import com.cs122b.client.SQLClient;
import com.cs122b.model.Playlist;
import com.cs122b.model.Track;
import com.cs122b.model.Album;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.naming.NamingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistService {

    static void setPlaylistTrackAttrs(SQLClient db, Track track, ResultSet query, boolean addPopularity)
            throws SQLException, NamingException {

        track.setId(query.getString("id"));
        track.setName(query.getString("name"));
        track.setTrack_number(query.getInt("track_number"));

        if (addPopularity) {
            track.setPopularity(query.getInt("popularity"));
        }

        String artistTrackQuery = "SELECT * FROM artist_in_track NATURAL JOIN artist WHERE artist_id = id AND track_id = ?;";
        PreparedStatement statement = db.getConnection().prepareStatement(artistTrackQuery, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, track.getId());
        ResultSet artistsResult = statement.executeQuery();

        while (artistsResult.next()) {
            if (artistsResult == null) {
                break;
            }
            com.cs122b.model.Artist artist = new com.cs122b.model.Artist();
            artist.setId(artistsResult.getString("id"));
            artist.setName(artistsResult.getString("name"));
            artist.setImage(artistsResult.getString("image"));

            track.addArtists(artist);
        }

        String trackArtistQuery = "SELECT * FROM track_in_album NATURAL JOIN album WHERE album_id = id AND track_id = ?;";
        statement = db.getConnection().prepareStatement(trackArtistQuery, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, track.getId());
        ResultSet albumResult = statement.executeQuery();

        albumResult.next();

        com.cs122b.model.Album album = new com.cs122b.model.Album();
        album.setId(albumResult.getString("id"));
        album.setName(albumResult.getString("name"));
        album.setAlbum_type(albumResult.getString("album_type"));
        album.setImage(albumResult.getString("image"));
        album.setRelease_date(albumResult.getString("release_date"));

        track.setAlbum(album);
    }

    private static void setPlaylistAttrs(SQLClient db, Playlist playlist, ResultSet result) throws SQLException, NamingException {

        playlist.setId(result.getInt("id"));
        playlist.setName(result.getString("name"));
        playlist.setImage(result.getString("image"));
        playlist.setCreation_date(result.getString("creation_date"));
        playlist.setPlaylistsCreated(result.getInt("playlistCreated"));

        // Get the tracks
        String query = "SELECT * FROM track_in_playlist\n"
                + "LEFT JOIN track ON track.id = track_in_playlist.track_id\n"
                + "LEFT JOIN track_meta ON track_meta.id = track.id\n" + "WHERE track_in_playlist.playlist_id = ?;";
        PreparedStatement statement = db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, playlist.getId());
        ResultSet resultTracks = statement.executeQuery();

        while (resultTracks.next()) {
            Track track = new Track();
            setPlaylistTrackAttrs(db, track, resultTracks, false);
            playlist.addTrack(track);
        }
    }

    private static void insertPlaylist(SQLClient db, Playlist playlist, int userId) throws SQLException, NamingException {

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
            insertTrackPlaylistQuery += "(" + playlist.getId() + ", \"" + playlist.getTracks().get(i).getId() + "\")"
                    + ((i < playlist.getTracks().size() - 1) ? "," : "");
        PreparedStatement insertTracksStatement = db.getConnection().prepareStatement(insertTrackPlaylistQuery,
                Statement.RETURN_GENERATED_KEYS);
        insertTracksStatement.executeUpdate();

        relationPstmt.close();
    }

    public static void insertSnapshot(String playlist_id, String snapshot_id) throws SQLException, NamingException {
        SQLClient db = new SQLClient();

        String insertQuery = "INSERT INTO playlist_spotify_snapshot(playlist_id, snapshot_id) VALUES (?, ?);";
        PreparedStatement pstmt = db.getConnection().prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, playlist_id);
        pstmt.setString(2, snapshot_id);
        pstmt.executeUpdate();

        db.closeConnection();
    }

    public static Playlist createPlaylist(JsonObject playlistJson, int userId) throws SQLException, NamingException {
        SQLClient db = new SQLClient();
        String name = playlistJson.get("name").getAsString();

        // Check if exists (return null if exists)
        String query = "SELECT *  FROM playlist NATURAL JOIN playlist_to_user WHERE id = playlist_id AND name=? AND user_id=?;";
        PreparedStatement statement = db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, name);
        statement.setInt(2, userId);
        ResultSet result = statement.executeQuery();

        if (result.next() != false) {
            db.closeConnection();
            return null;
        }

        // Create playlist
        Playlist playlist = new Playlist();
        playlist.setName(name);
        playlist.setUser_id(userId);
        playlist.setImage(playlistJson.get("image").getAsString());
        JsonArray tracks = playlistJson.getAsJsonArray("tracks");

        for (int i = 0; i < tracks.size(); i++) {
            JsonObject track = (JsonObject) tracks.get(i);
            Track newTrack = new Track();
            newTrack.setId(track.get("id").getAsString());
            playlist.addTrack(newTrack);
        }

        insertPlaylist(db, playlist, userId);

        db.closeConnection();
        return playlist;
    }

    public static Playlist fetchPlaylist(int id, int userId) throws SQLException, NamingException {
        SQLClient db = new SQLClient();
        Playlist playlist = new Playlist();


        String query = "SELECT *, 1 - ISNULL(snapshot_id) as playlistCreated FROM playlist NATURAL JOIN playlist_to_user"
                        + "LEFT JOIN playlist_spotify_snapshot ON playlist_spotify_snapshot.playlist_id = playlist.id  "
                        + "WHERE id=? AND id = playlists_to_user.playlist_id AND user_id=?";
        PreparedStatement statement = db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, id);
        statement.setInt(2, userId);
        ResultSet result = statement.executeQuery();

        if (result.next() == false) {
            db.closeConnection();
            return null;
        }

        setPlaylistAttrs(db, playlist, result);
        db.closeConnection();
        return playlist;
    }

    public static List<Playlist> fetchPlaylists(int userId, int offset, int limit) throws SQLException, NamingException {
        SQLClient db = new SQLClient();
        List<Playlist> playlists = new ArrayList<Playlist>();

        String query = "SELECT *, 1 - ISNULL(snapshot_id) as playlistCreated FROM playlist "
                        + "LEFT JOIN playlist_to_user ON playlist_to_user.playlist_id = playlist.id "
                        + "LEFT JOIN playlist_spotify_snapshot ON playlist_spotify_snapshot.playlist_id = playlist.id "
                        + "WHERE playlist_to_user.user_id=? ORDER BY creation_date DESC LIMIT 10";
        PreparedStatement statement = db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, userId);
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            Playlist playlist = new Playlist();
            setPlaylistAttrs(db, playlist, result);
            playlists.add(playlist);
        }

        statement.close();
        db.closeConnection();
        return playlists;
    }
}
