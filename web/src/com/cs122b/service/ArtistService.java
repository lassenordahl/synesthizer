package com.cs122b.service;

import com.cs122b.client.Query;
import com.cs122b.client.SQLClient;
import com.cs122b.model.Album;
import com.cs122b.model.Artist;
import com.cs122b.utils.StringUtil;

import javax.naming.NamingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ArtistService {

    private static void setArtistAttrs(SQLClient db, Artist artist, ResultSet query, Boolean setPopularity)
            throws SQLException, NamingException {
        artist.setId(query.getString("id"));
        artist.setName(query.getString("name"));
        artist.setImage(query.getString("image"));

        if (setPopularity == true) {
            artist.setPopularity(query.getInt("popularity"));
        }

        String artistGenreQuery = "SELECT * FROM artist_in_genre WHERE artist_id = ? ORDER BY genre";
        PreparedStatement statement = db.getConnection().prepareStatement(artistGenreQuery,
                Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, artist.getId());

        ResultSet genreResult;
        try {
            genreResult = statement.executeQuery();
        } catch(SQLException e) {
            statement.close();
            db.closeConnection();
            e.printStackTrace();
            throw e;
        }

        while (genreResult.next()) {
            artist.addGenre(genreResult.getString("genre"));
        }

        String artistAlbumQuery = "SELECT * FROM artist_in_album NATURAL JOIN album WHERE album_id = id AND artist_id = ? ORDER BY name asc";
        PreparedStatement albumsStatement = db.getConnection().prepareStatement(artistAlbumQuery,
                Statement.RETURN_GENERATED_KEYS);
        albumsStatement.setString(1, artist.getId());

        ResultSet albumResult;
        try {
            albumResult = albumsStatement.executeQuery();
        } catch(SQLException e) {
            statement.close();
            db.closeConnection();
            e.printStackTrace();
            throw e;
        }

        while (albumResult.next()) {
            Album album = new Album();
            album.setId(albumResult.getString("id"));
            album.setName(albumResult.getString("name"));
            album.setAlbum_type(albumResult.getString("album_type"));
            album.setImage(albumResult.getString("image"));
            album.setRelease_date(albumResult.getString("release_date"));
            artist.addAlbum(album);
        }

        statement.close();
        albumsStatement.close();
    }

    public static String insertArtist(String id, String name, String image) throws SQLException, NamingException {
        SQLClient db = new SQLClient();

        String query = "SELECT insert_artist(?, ?, ?) as result";
        PreparedStatement pstmt = db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, id);
        pstmt.setString(2, name);
        pstmt.setString(3, image);
        ResultSet result = pstmt.executeQuery();

        result.next();

        String response = result.getString("result");

        pstmt.close();
        db.closeConnection();
        return response;
    }

    public static List<Artist> fetchArtists(int offset, int limit, String sortBy, String searchMode, String search,
            String subMode, String name, String genre) throws SQLException, NamingException {
        SQLClient db = new SQLClient();

        StringBuilder queryString = new StringBuilder();
        ArrayList<String> parameters = new ArrayList<String>();
        ArrayList<String> paramTypes = new ArrayList<String>();

        // SELECT
        queryString.append("SELECT DISTINCT artist.id, artist.name, artist.image, ");
        queryString.append(
                "IFNULL((SELECT COUNT(track_in_playlist.playlist_id) FROM artist_in_track LEFT JOIN track_in_playlist ON track_in_playlist.track_id = artist_in_track.track_id ");
        queryString.append(
                "WHERE artist_in_track.artist_id = artist.id GROUP BY artist_in_track.artist_id), 0) as popularity ");

        // FROM
        queryString.append("FROM artist LEFT JOIN artist_in_genre ON artist.id = artist_in_genre.artist_id ");

        // WHERE
        if (searchMode != null && search != null) {
            if (searchMode.equals("name")) {
                searchMode = "artist.name";
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
            }
        } else if (name != null && name != "" || genre != null && genre != "") {
            queryString.append("WHERE ");
            if (name != null && name != "") {
                queryString.append("artist.name LIKE ? ");
                parameters.add(name + "%");
                paramTypes.add("string");

                if (genre != null && genre != "") {
                    queryString.append("AND ");
                }
            }
            if (genre != null && genre != "") {
                queryString.append("artist_in_genre.genre LIKE ? ");
                parameters.add(genre);
                paramTypes.add("string");
            }
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

        List<Artist> artists = new ArrayList<Artist>();
        while (result.next()) {
            Artist artist = new Artist();
            setArtistAttrs(db, artist, result, true);
            artists.add(artist);
        }

        db.closeConnection();
        return artists;
    }

    public static Artist fetchArtist(String id) throws SQLException, NamingException {
        SQLClient db = new SQLClient();

        String query = "SELECT * FROM artist WHERE id = ?";
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
        Artist artist = new Artist();
        setArtistAttrs(db, artist, result, false);

        statement.close();
        db.closeConnection();
        return artist;
    }
}
