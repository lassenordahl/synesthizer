package com.cs122b.service;

import com.cs122b.client.Query;
import com.cs122b.client.SQLClient;
import com.cs122b.model.Album;
import com.cs122b.model.Artist;
import com.cs122b.model.Track;
import com.cs122b.utils.StringUtil;

import javax.naming.NamingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlbumService {

    public static void setAlbumAttrs(SQLClient db, Album album, ResultSet result, boolean addPopularity)
            throws SQLException, NamingException {
        album.setId(result.getString("id"));
        album.setName(result.getString("name"));
        album.setAlbum_type(result.getString("album_type"));
        album.setImage(result.getString("image"));
        album.setRelease_date(result.getString("release_date"));

        // Add Artists to Album
        String query = "SELECT * FROM artist_in_album " + "NATURAL JOIN artist \n"
                + "WHERE artist_id = id AND album_id = ?\n" + "ORDER BY name;";
        PreparedStatement statement = db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, album.getId());
         
        ResultSet artistsResult;
        try {
            artistsResult = statement.executeQuery();
        } catch(SQLException e) {
            statement.close();
            db.closeConnection();
            e.printStackTrace();
            throw e;
        }

        while (artistsResult.next()) {
            if (artistsResult == null) {
                break;
            }
            Artist artist = new Artist();
            artist.setId(artistsResult.getString("id"));
            artist.setName(artistsResult.getString("name"));
            artist.setImage(artistsResult.getString("image"));

            album.addArtists(artist);
        }

        if (addPopularity) {
            album.setPopularity(result.getInt("popularity"));
        }
    }

    public static List<Album> fetchAlbums(int offset, int limit, String sortBy, String searchMode, String search,
            String subMode, String name, String artist_id) throws SQLException, NamingException {
        SQLClient db = new SQLClient();

        StringBuilder queryString = new StringBuilder();
        ArrayList<String> parameters = new ArrayList<String>();
        ArrayList<String> paramTypes = new ArrayList<String>();

        // SELECT
        queryString
                .append("SELECT DISTINCT album.id, album.name, album.album_type, album.image, album.release_date,  ");
        queryString.append("(SELECT COUNT(track_in_playlist.playlist_id) FROM track_in_album "
                + "LEFT JOIN track_in_playlist ON track_in_playlist.track_id = track_in_album.track_id "
                + "WHERE track_in_album.album_id = album.id GROUP BY track_in_album.album_id) as popularity ");

        // FROM
        queryString.append("FROM album LEFT JOIN artist_in_album ON artist_in_album.album_id = album.id "
                + "LEFT JOIN artist ON artist.id = artist_in_album.artist_id ");

        // WHERE
        if (name != null && name != "") {
            queryString.append("WHERE album.name LIKE ? ");
            parameters.add(name + "%");
            paramTypes.add("string");
        } else if (artist_id != null && artist_id != "") {
            queryString.append("WHERE artist.id LIKE \"" + artist_id + "\" ");
        } else if (searchMode != null && search != null && searchMode.equals("name")) {
            searchMode = "album.name";
            queryString.append("WHERE MATCH (" + searchMode + ") AGAINST (? IN BOOLEAN MODE)");
            
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

        String query = queryString.toString();
        PreparedStatement statement = db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        for (int i = 0; i < parameters.size(); i++) {
            if (paramTypes.get(i).equalsIgnoreCase("string")) {
                statement.setString(i + 1, parameters.get(i));
            } else {
                statement.setInt(i + 1, Integer.parseInt(parameters.get(i)));
            }
        }

        ResultSet result;
        try {
            result = statement.executeQuery();
        } catch(SQLException e) {
            statement.close();
            db.closeConnection();
            e.printStackTrace();
            throw e;
        }

        List<Album> albums = new ArrayList<Album>();

        while (result.next()) {
            Album album = new Album();
            setAlbumAttrs(db, album, result, true);
            albums.add(album);
        }

        db.closeConnection();
        return albums;
    }

    public static String insertAlbum(String id, String name, String image, String album_type, String release_date,
            String artist_id) throws SQLException, NamingException {
        SQLClient db = new SQLClient();

        String query = "SELECT insert_album(?, ?, ?, ?, ?, ?) as result";
        PreparedStatement pstmt = db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, id);
        pstmt.setString(2, name);
        pstmt.setString(3, album_type);
        pstmt.setString(4, image);
        pstmt.setString(5, release_date);
        pstmt.setString(6, artist_id);
        ResultSet result = pstmt.executeQuery();

        result.next();

        String response = result.getString("result");

        pstmt.close();
        db.closeConnection();
        return response;
    }

    public static Album fetchAlbum(String id) throws SQLException, NamingException {

        SQLClient db = new SQLClient();

        String query = "SELECT album.id, album.name, album.album_type, album.image, album.release_date, "
                + "artist.name as artist_name, artist.id as artist_id FROM album\n"
                + "LEFT JOIN artist_in_album as a_to_a ON a_to_a.album_id = album.id\n"
                + "LEFT JOIN artist ON a_to_a.artist_id = artist.id \n" + "WHERE album.id = ?";

        PreparedStatement statement = db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, id);

        ResultSet result;
        try {
            result = statement.executeQuery();
        } catch(SQLException e) {
            statement.close();
            db.closeConnection();
            e.printStackTrace();
            throw e;
        }

        result.next();

        Album album = new Album();
        setAlbumAttrs(db, album, result, false);

        db.closeConnection();
        return album;
    }

    public static List<Track> fetchTracksForAlbum(String id) throws SQLException, NamingException {

        SQLClient db = new SQLClient();
        List<Track> tracksForAlbum = new ArrayList<Track>();

        String query = "SELECT track.id, track.name, track_meta.duration_ms, track.track_number FROM album\n"
                + "LEFT JOIN track_in_album ON track_in_album.album_id = album.id\n"
                + "LEFT JOIN track ON track.id = track_in_album.track_id\n"
                + "LEFT JOIN track_meta ON track_meta.id = track.id WHERE album.id = ?\n"
                + "ORDER BY track.track_number;";
        PreparedStatement statement = db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, id);
        
        ResultSet result;
        try {
            result = statement.executeQuery();
        } catch(SQLException e) {
            statement.close();
            db.closeConnection();
            e.printStackTrace();
            throw e;
        }

        while (result.next()) {
            Track track = new Track();

            track.setId(result.getString("id"));
            track.setName(result.getString("name"));
            track.setDuration_ms(result.getInt("duration_ms"));
            track.setTrack_number(result.getInt("track_number"));

            tracksForAlbum.add(track);
        }

        db.closeConnection();
        return tracksForAlbum;
    }
}
