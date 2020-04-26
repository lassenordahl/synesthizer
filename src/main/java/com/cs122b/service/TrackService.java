package com.cs122b.service;

import com.cs122b.client.Config;
import com.cs122b.client.Query;
import com.cs122b.client.SQLClient;
import com.cs122b.model.Album;
import com.cs122b.model.Track;
import com.cs122b.model.Artist;
import com.cs122b.model.TrackMeta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrackService implements Config {

    private static SQLClient db;

    static void setTrackAttrs(Track track, ResultSet query, boolean addPopularity) throws SQLException {
        track.setId(query.getString("id"));
        track.setName(query.getString("name"));
        track.setTrack_number(query.getInt("track_number"));

        if (addPopularity) {
            track.setPopularity(query.getInt("popularity"));
        }

        Query queryArtist = db
                .query("SELECT * FROM artist_in_track NATURAL JOIN artist WHERE artist_id = id AND track_id = \""
                        + track.getId() + "\";");
        ResultSet artistsResult = queryArtist.getResult();

        while (artistsResult.next()) {
            if (artistsResult == null) {
                break;
            }
            Artist artist = new Artist();
            artist.setId(artistsResult.getString("id"));
            artist.setName(artistsResult.getString("name"));
            artist.setImage(artistsResult.getString("image"));

            track.addArtists(artist);
        }

        queryArtist.closeQuery();

        Query queryAlbum = db
                .query("SELECT * FROM track_in_album NATURAL JOIN album WHERE album_id = id AND track_id = \""
                        + track.getId() + "\";");

        ResultSet albumResult = queryAlbum.getResult();
        albumResult.next();

        Album album = new Album();
        album.setId(albumResult.getString("id"));
        album.setName(albumResult.getString("name"));
        album.setAlbum_type(albumResult.getString("album_type"));
        album.setImage(albumResult.getString("image"));
        album.setRelease_date(albumResult.getString("release_date"));

        track.setAlbum(album);

        queryAlbum.closeQuery();
    }

    private static void setTrackMeta(TrackMeta trackMeta, ResultSet result) throws SQLException {
        trackMeta.setAcousticness(result.getFloat("acousticness"));
        trackMeta.setAnalysis_url(result.getString("analysis_url"));
        trackMeta.setDanceability(result.getFloat("danceability"));
        trackMeta.setDuration_ms(result.getInt("duration_ms"));
        trackMeta.setEnergy(result.getFloat("energy"));
        trackMeta.setInstrumentalness(result.getFloat("instrumentalness"));
        trackMeta.setNote(result.getInt("note"));
        trackMeta.setLiveness(result.getFloat("liveness"));
        trackMeta.setLoudness(result.getFloat("loudness"));
        trackMeta.setMode(result.getInt("mode"));
        trackMeta.setSpeechiness(result.getFloat("speechiness"));
        trackMeta.setTempo(result.getFloat("tempo"));
        trackMeta.setTime_signature(result.getInt("time_signature"));
        trackMeta.setTrack_href(result.getString("track_href"));
        trackMeta.setType(result.getString("type"));
        trackMeta.setValence(result.getFloat("valence"));
    }

    public static List<Track> fetchTracks(int offset, int limit, String sortBy) throws SQLException {
        // Create an execute an SQL statement to select all of table tracks records

        db = new SQLClient();

        // Query query = db.query("SELECT *, \n" + "IFNULL((\n"
        // + "SELECT COUNT(tip.playlist_id) FROM track_in_playlist as tip\n" + "WHERE
        // tip.track_id = track.id\n"
        // + "GROUP BY tip.track_id\n" + "), 0) as popularity FROM track\n"
        // + "LEFT JOIN track_meta ON track.id = track_meta.id\n" + "ORDER BY popularity
        // DESC\n" + "LIMIT "
        // + Integer.toString(offset) + "," + Integer.toString(limit));

        StringBuilder queryString = new StringBuilder();

        // SELECT
        queryString.append(
                "SELECT *, IFNULL((SELECT COUNT(tip.playlist_id) FROM track_in_playlist as tip WHERE tip.track_id = track.id GROUP BY tip.track_id), 0) as popularity ");

        // FROM
        queryString.append("FROM track LEFT JOIN track_meta ON track.id = track_meta.id ");

        // WHERE

        // ORDER BY
        queryString.append("ORDER BY " + sortBy + " ");

        // LIMIT/OFFSET
        queryString.append("LIMIT " + Integer.toString(offset) + "," + Integer.toString(limit));

        System.out.println(queryString.toString());

        Query query = db.query(queryString.toString());

        List<Track> tracks = new ArrayList<Track>();
        ResultSet result = query.getResult();
        while (result.next()) {
            Track track = new Track();
            setTrackAttrs(track, result, true);
            tracks.add(track);
        }
        query.closeQuery();

        db.closeConnection();
        return tracks;
    }

    public static Track fetchTrack(String id) throws SQLException {
        // Create an execute an SQL statement to select all of table tracks records

        db = new SQLClient();

        Query query = db.query(String.format("SELECT * FROM track WHERE track.id = '%s'", id));

        ResultSet result = query.getResult();
        result.next();
        Track track = new Track();
        setTrackAttrs(track, result, false);

        query.closeQuery();
        db.closeConnection();
        return track;
    }

    public static TrackMeta fetchTrackMeta(String id) throws SQLException {
        db = new SQLClient();

        Query query = db.query("SELECT * FROM track_meta\n" + "WHERE track_meta.id = \"" + id + "\"");

        TrackMeta trackMeta = new TrackMeta();
        ResultSet result = query.getResult();

        result.next();

        setTrackMeta(trackMeta, result);

        query.closeQuery();
        db.closeConnection();

        return trackMeta;
    }
}
