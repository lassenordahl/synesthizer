package com.cs122b.service;

import com.cs122b.client.Config;
import com.cs122b.client.Query;
import com.cs122b.client.SQLClient;
import com.cs122b.model.Album;
import com.cs122b.model.Track;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlbumService implements Config {

    private static SQLClient db;

    public static void setAlbumAttrs(Album album, ResultSet result, boolean addPopularity) throws SQLException {
        album.setId(result.getString("id"));
        album.setName(result.getString("name"));
        album.setAlbum_type(result.getString("album_type"));
        album.setImage(result.getString("image"));
        album.setRelease_date(result.getString("release_date"));
        album.setArtist_name(result.getString("artist_name"));
        album.setArtist_id(result.getString("artist_id"));

        if (addPopularity) {
            album.setPopularity(result.getInt("popularity"));
        }
    }

    public static List<Album> fetchAlbums(int offset, int limit) throws SQLException {
        db = new SQLClient();

        Query query = db.query("SELECT album.id, album.name, album.album_type, album.image, album.release_date, (\n"
                + "\tSELECT COUNT(track_in_playlist.playlist_id) FROM track_in_album\n"
                + "\tLEFT JOIN track_in_playlist ON track_in_playlist.track_id = track_in_album.track_id\n"
                + "\tWHERE track_in_album.album_id = album.id\n" + "    GROUP BY track_in_album.album_id\n"
                + ")as popularity, artist.name as artist_name, artist.id as artist_id FROM album\n"
                + "LEFT JOIN artist_in_album ON artist_in_album.album_id = album.id\n"
                + "LEFT JOIN artist ON artist.id = artist_in_album.artist_id\n" + "ORDER BY popularity DESC\n"
                + "LIMIT 0," + Integer.toString(limit) + ";");
        ResultSet result = query.getResult();

        List<Album> albums = new ArrayList<Album>();

        while (result.next()) {
            Album album = new Album();
            setAlbumAttrs(album, result, true);
            albums.add(album);
        }

        query.closeQuery();
        db.closeConnection();

        return albums;
    }

    public static Album fetchAlbum(String id) throws SQLException {

        db = new SQLClient();

        Query query = db.query("SELECT album.id, album.name, album.album_type, album.image, album.release_date, "
                + "artist.name as artist_name, artist.id as artist_id FROM album\n"
                + "LEFT JOIN artist_in_album as a_to_a ON a_to_a.album_id = album.id\n"
                + "LEFT JOIN artist ON a_to_a.artist_id = artist.id \n" + "WHERE album.id = \"" + id + "\"");

        ResultSet result = query.getResult();
        result.next();

        Album album = new Album();
        setAlbumAttrs(album, result, false);
        query.closeQuery();
        db.closeConnection();
        return album;
    }

    public static List<Track> fetchTracksForAlbum(String id) throws SQLException {

        db = new SQLClient();

        Query query = db.query("SELECT track.id, track.name, track_meta.duration_ms, track.track_number FROM album\n"
                + "LEFT JOIN track_in_album ON track_in_album.album_id = album.id\n"
                + "LEFT JOIN track ON track.id = track_in_album.track_id\n"
                + "LEFT JOIN track_meta ON track_meta.id = track.id WHERE album.id = \"" + id + "\";");

        List<Track> tracksForAlbum = new ArrayList<Track>();
        ResultSet result = query.getResult();
        while (result.next()) {
            Track track = new Track();

            track.setId(result.getString("id"));
            track.setName(result.getString("name"));
            track.setDuration_ms(result.getInt("duration_ms"));
            track.setTrack_number(result.getInt("track_number"));

            tracksForAlbum.add(track);
        }
        query.closeQuery();

        db.closeConnection();
        return tracksForAlbum;
    }
}
