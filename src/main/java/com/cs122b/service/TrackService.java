package com.cs122b.service;

import com.cs122b.client.Config;
import com.cs122b.client.Query;
import com.cs122b.client.SQLClient;
import com.cs122b.model.Album;
import com.cs122b.model.Track;
import com.cs122b.model.Artist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrackService implements Config {

    private static SQLClient db;

    private static void setTrackAttrs(Track track, ResultSet query, boolean addPopularity) throws SQLException {
        track.setId(query.getString("id"));
        track.setName(query.getString("name"));
        track.setTrack_number(query.getInt("track_number"));
        track.setAcousticness(query.getFloat("acousticness"));
        track.setAnalysis_url(query.getString("analysis_url"));
        track.setDanceability(query.getFloat("danceability"));
        track.setDuration_ms(query.getInt("duration_ms"));
        track.setEnergy(query.getFloat("energy"));
        track.setInstrumentalness(query.getFloat("instrumentalness"));
        track.setNote(query.getInt("note"));
        track.setLiveness(query.getFloat("liveness"));
        track.setLoudness(query.getFloat("loudness"));
        track.setMode(query.getInt("mode"));
        track.setSpeechiness(query.getFloat("speechiness"));
        track.setTempo(query.getFloat("tempo"));
        track.setTime_signature(query.getInt("time_signature"));
        track.setTrack_href(query.getString("track_href"));
        track.setType(query.getString("type"));
        track.setValence(query.getFloat("valence"));
        if (addPopularity) {
            track.setPopularity(query.getInt("popularity"));
        }

        Query queryArtist = db
                .query("SELECT * FROM artist_in_track NATURAL JOIN artist WHERE artist_id = id AND track_id = \""
                        + track.getId() + "\"");
        ResultSet artistsResult = queryArtist.getResult();

        while (artistsResult.next()) {
            Artist artist = new Artist();
            artist.setId(artistsResult.getString("id"));
            artist.setName(artistsResult.getString("name"));
            artist.setImage(artistsResult.getString("image"));

            track.addArtists(artist);
        }
        queryArtist.closeQuery();

        Query queryAlbum = db
                .query("SELECT * FROM track_in_album NATURAL JOIN album WHERE album_id = id AND track_id = \""
                        + track.getId() + "\"");

        ResultSet albumResult = queryAlbum.getResult();
        albumResult.next();

        track.setAlbum(new Album(albumResult.getString("id"), albumResult.getString("name"),
                albumResult.getString("album_type"), albumResult.getString("image"),
                albumResult.getString("release_date"), null, null));

        queryAlbum.closeQuery();
    }

    public static List<Track> fetchTracks(int offset, int limit, String sortBy) throws SQLException {
        // Create an execute an SQL statement to select all of table tracks records

        db = new SQLClient();

        Query query = db.query("SELECT *, \n" +
                "IFNULL((\n" +
                "SELECT COUNT(tip.playlist_id) FROM track_in_playlist as tip\n" +
                "WHERE tip.track_id = track.id\n" +
                "GROUP BY tip.track_id\n" +
                "), 0) as popularity FROM track\n" +
                "LEFT JOIN track_meta ON track.id = track_meta.id\n" +
                "ORDER BY popularity DESC\n" +
                "LIMIT " + Integer.toString(offset) + "," + Integer.toString(limit));

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

        Query query = db.query("SELECT * FROM track NATURAL JOIN track_meta WHERE track.id = \"" + id + "\"");

        ResultSet result = query.getResult();
        result.next();
        Track track = new Track();
        setTrackAttrs(track, result, false);

        query.closeQuery();
        db.closeConnection();
        return track;
    }
}
