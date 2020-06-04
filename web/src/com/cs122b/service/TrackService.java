package com.cs122b.service;

import com.cs122b.client.Query;
import com.cs122b.client.SQLClient;
import com.cs122b.model.Album;
import com.cs122b.model.Track;
import com.cs122b.model.Artist;
import com.cs122b.model.TrackMeta;
import com.cs122b.utils.StringUtil;
import com.google.gson.internal.bind.SqlDateTypeAdapter;
import com.cs122b.utils.MyLogger;

import javax.naming.NamingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrackService {

    static void setTrackAttrs(SQLClient db, Track track, ResultSet query, boolean addPopularity) throws SQLException, NamingException {

        track.setId(query.getString("id"));
        track.setName(query.getString("name"));
        track.setTrack_number(query.getInt("track_number"));

        if (addPopularity) {
            track.setPopularity(query.getInt("popularity"));
        }

        String queryArtist = "SELECT * FROM artist_in_track NATURAL JOIN artist WHERE artist_id = id AND track_id = ? ORDER BY name;";
        PreparedStatement statement = db.getConnection().prepareStatement(queryArtist, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, track.getId());
        ResultSet artistsResult = statement.executeQuery();

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

        Album album = new Album();
        album.setId(query.getString("album_id"));
        album.setName(query.getString("album_name"));
        album.setAlbum_type(query.getString("album_type"));
        album.setImage(query.getString("album_image"));
        album.setRelease_date(query.getString("release_date"));

        track.setAlbum(album);
    }

    // track_id VARCHAR(25), name VARCHAR(100), track_number INT, album_id
    // VARCHAR(25), artist_id VARCHAR(25)
    public static String insertTrack(String id, String name, int track_number, int duration_ms, String album_id,
            String artist_id) throws SQLException, NamingException {
        SQLClient db = new SQLClient();

        String query = "SELECT insert_track(?, ?, ?, ?, ?, ?) as result";
        PreparedStatement pstmt = db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, id);
        pstmt.setString(2, name);
        pstmt.setInt(3, track_number);
        pstmt.setInt(4, duration_ms);
        pstmt.setString(5, album_id);
        pstmt.setString(6, artist_id);
        ResultSet result = pstmt.executeQuery();

        result.next();

        String response = result.getString("result");

        pstmt.close();
        db.closeConnection();
        return response;
    }

    private static void setTrackMeta(TrackMeta trackMeta, ResultSet result) throws SQLException, NamingException {
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

    public static List<Track> fetchTracks(boolean pooling, int offset, int limit, String sortBy, String searchMode, String search,
            String subMode, String name, String logTime) throws SQLException, NamingException {
        // Create an execute an SQL statement to select all of table tracks records


        SQLClient db;
        if (!pooling) {
            db = new SQLClient(false);
        } else {
            db = new SQLClient();
        }

        StringBuilder queryString = new StringBuilder();
        ArrayList<String> parameters = new ArrayList<String>();
        ArrayList<String> paramTypes = new ArrayList<String>();

        // SELECT
        queryString.append("SELECT DISTINCT track.*, track_meta.*, ");
        queryString.append(
                "album.id as album_id, album.name as album_name, album.album_type as album_type, album.image as album_image, album.release_date as release_date, ");
        queryString.append(
                "IFNULL((SELECT COUNT(tip.playlist_id) FROM track_in_playlist as tip WHERE tip.track_id = track.id GROUP BY tip.track_id), 0) as popularity ");

        // FROM
        queryString.append(
                "FROM track LEFT JOIN artist_in_track ON track.id = artist_in_track.track_id LEFT JOIN artist ON artist_in_track.artist_id = artist.id ");
        queryString.append("LEFT JOIN track_meta ON track.id = track_meta.id ");
        queryString.append("LEFT JOIN track_in_album ON track.id = track_in_album.track_id ");
        queryString.append("LEFT JOIN album ON track_in_album.album_id = album.id ");

        // WHERE
        if (name != null && name != "") {
            queryString.append("WHERE track.name LIKE ? ");
            parameters.add(name + "%");
            paramTypes.add("string");
        } else if (searchMode != null && search != null && searchMode.equals("name")) {
            searchMode = "track.name";
            queryString.append(
                    "WHERE MATCH (" + searchMode + ") AGAINST (? IN BOOLEAN MODE)");

            if (subMode != null && subMode.equals("fuzzy")) {
                queryString.append(
                    " OR fuzzy("+ searchMode +", ?)");
            }
            
            parameters.add(StringUtil.formatFullTextSearch(search));
            paramTypes.add("string");
            
            if (subMode != null && subMode.equals("fuzzy")) {
                parameters.add(search);
                paramTypes.add("string");
            }
        } else if (searchMode != null && search != null) {
            if (searchMode.equals("release_date")) {
                searchMode = "album.release_date";
            } else if (searchMode.equals("album_name")) {
                searchMode = "album.name";
            } else if (searchMode.equals("artist_name")) {
                searchMode = "artist.name";
            }

            queryString.append("WHERE " + searchMode + " LIKE ? ");
            parameters.add("%" + search + "%");
            paramTypes.add("string");
        }

        // ORDER BY
        if (subMode != null && subMode.equals("fuzzy")) {
            queryString.append(
                    "ORDER BY MATCH (" + searchMode + ") AGAINST (? IN BOOLEAN MODE) DESC ");
            parameters.add(search);
            paramTypes.add("string");
        } else {
            queryString.append("ORDER BY " + sortBy + " ");
        }

        // LIMIT/OFFSET
        queryString.append("LIMIT ?,?");
        parameters.add(Integer.toString(offset));
        parameters.add(Integer.toString(limit));
        paramTypes.add("int");
        paramTypes.add("int");

        long startTime = System.nanoTime();
        String query = queryString.toString();
        PreparedStatement statement = db.getConnection(pooling).prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        for (int i = 0; i < parameters.size(); i++) {
            if (paramTypes.get(i).equalsIgnoreCase("string")) {
                statement.setString(i + 1, parameters.get(i));
            } else {
                statement.setInt(i + 1, Integer.parseInt(parameters.get(i)));
            }
        }
        ResultSet result = statement.executeQuery();
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime; // elapsed time in nano seconds. Note: print the values in nano seconds 

        if (logTime != null) {
            MyLogger.log(String.format("[%s]: Tj: %s", logTime, Long.toString(elapsedTime)));
        }


        List<Track> tracks = new ArrayList<Track>();
        while (result.next()) {
            Track track = new Track();
            setTrackAttrs(db, track, result, true);
            tracks.add(track);
        }

        statement.close();
        db.closeConnection();
        return tracks;
    }

    public static Track fetchTrack(String id) throws SQLException, NamingException {
        // Create an execute an SQL statement to select all of table tracks records

        SQLClient db = new SQLClient();

        StringBuilder queryString = new StringBuilder();

        // SELECT
        queryString.append("SELECT track.*, ");
        queryString.append(
                "album.id as album_id, album.name as album_name, album.album_type as album_type, album.image as album_image, album.release_date as release_date ");

        // FROM
        queryString.append("FROM track LEFT JOIN track_in_album ON track.id = track_in_album.track_id ");
        queryString.append("LEFT JOIN album ON track_in_album.album_id = album.id ");

        // WHERE
        queryString.append("WHERE track.id = ?");

        System.out.println(queryString.toString());

        String query = queryString.toString();
        PreparedStatement statement = db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, id);
        ResultSet result = statement.executeQuery();

        result.next();
        Track track = new Track();
        setTrackAttrs(db, track, result, false);

        db.closeConnection();
        return track;
    }

    public static TrackMeta fetchTrackMeta(String id) throws SQLException, NamingException {
        SQLClient db = new SQLClient();
        TrackMeta trackMeta = new TrackMeta();

        String query = "SELECT * FROM track_meta WHERE track_meta.id = ?;";
        PreparedStatement statement = db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, id);
        ResultSet result = statement.executeQuery();

        result.next();
        setTrackMeta(trackMeta, result);

        db.closeConnection();
        return trackMeta;
    }
}
