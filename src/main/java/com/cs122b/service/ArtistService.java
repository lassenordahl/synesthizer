package com.cs122b.service;

import com.cs122b.client.Query;
import com.cs122b.client.SQLClient;
import com.cs122b.model.Album;
import com.cs122b.model.Artist;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArtistService {

    private static SQLClient db;

    private static void setArtistAttrs(Artist artist, ResultSet query, Boolean setPopularity) throws SQLException {
        artist.setId(query.getString("id"));
        artist.setName(query.getString("name"));
        artist.setImage(query.getString("image"));

        if (setPopularity == true) {
            artist.setPopularity(query.getInt("popularity"));
        }

        Query queryGenres = db
                .query("SELECT * FROM artist_in_genre WHERE artist_id = \"" + artist.getId() + "\" ORDER BY genre");
        ResultSet genreResult = queryGenres.getResult();
        while (genreResult.next()) {
            artist.addGenre(genreResult.getString("genre"));
        }
        queryGenres.closeQuery();

        Query queryAlbums = db
                .query("SELECT * FROM artist_in_album NATURAL JOIN album WHERE album_id = id AND artist_id = \""
                        + artist.getId() + "\" ORDER BY name asc");
        ResultSet albumResult = queryAlbums.getResult();
        while (albumResult.next()) {
            Album album = new Album();

            album.setId(albumResult.getString("id"));
            album.setName(albumResult.getString("name"));
            album.setAlbum_type(albumResult.getString("album_type"));
            album.setImage(albumResult.getString("image"));
            album.setRelease_date(albumResult.getString("release_date"));
            album.setArtist_name(artist.getName());
            album.setArtist_id(artist.getId());

            artist.addAlbum(album);
        }
        queryAlbums.closeQuery();
    }

    public static List<Artist> fetchArtists(int offset, int limit, String sortBy, String searchMode, String search,
            String name, String genre) throws SQLException {
        db = new SQLClient();

        // Query query = db.query("SELECT * FROM artist ORDER BY " + sortBy + " LIMIT "
        // + Integer.toString(offset) + ","
        // + Integer.toString(limit));

        StringBuilder queryString = new StringBuilder();

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
            }

            queryString.append("WHERE " + searchMode + " LIKE \"%" + search + "%\" ");
        } else if (name != null && name != "" || genre != null && genre != "") {
            queryString.append("WHERE ");
            if (name != null && name != "") {
                queryString.append("artist.name LIKE \"" + name + "%\" ");

                if (genre != null && genre != "") {
                    queryString.append("AND ");
                }
            }
            if (genre != null && genre != "") {
                queryString.append("artist_in_genre.genre LIKE \"" + genre + "\" ");
            }
        }

        // ORDER BY
        queryString.append("ORDER BY " + sortBy + " ");

        // LIMIT/OFFSET
        queryString.append("LIMIT " + Integer.toString(offset) + "," + Integer.toString(limit));

        System.out.println(queryString.toString());

        Query query = db.query(queryString.toString());

        List<Artist> artists = new ArrayList<Artist>();
        ResultSet result = query.getResult();
        while (result.next()) {
            Artist artist = new Artist();
            setArtistAttrs(artist, result, true);
            artists.add(artist);
        }
        query.closeQuery();

        db.closeConnection();
        return artists;
    }

    public static Artist fetchArtist(String id) throws SQLException {
        db = new SQLClient();

        Query query = db.query("SELECT * FROM artist WHERE id = \"" + id + "\"");

        ResultSet result = query.getResult();
        result.next();
        Artist artist = new Artist();
        setArtistAttrs(artist, result, false);

        query.closeQuery();
        db.closeConnection();
        return artist;
    }
}
