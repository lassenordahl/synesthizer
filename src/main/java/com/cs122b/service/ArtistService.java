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

    private static void setArtistAttrs(Artist artist, ResultSet query) throws SQLException {
        artist.setId(query.getString("id"));
        artist.setName(query.getString("name"));
        artist.setImage(query.getString("image"));

        Query queryGenres = db.query("SELECT * FROM artist_in_genre WHERE artist_id = \"" + artist.getId() + "\"");
        ResultSet genreResult = queryGenres.getResult();
        while(genreResult.next()) {
            artist.addGenre(genreResult.getString("genre"));
        }
        queryGenres.closeQuery();

        Query queryAlbums = db.query("SELECT * FROM artist_in_album NATURAL JOIN album WHERE album_id = id AND artist_id = \"" + artist.getId() + "\"");
        ResultSet albumResult = queryAlbums.getResult();
        while(albumResult.next()) {
            artist.addAlbum(new Album(albumResult.getString("id"),
                    albumResult.getString("name"),
                    albumResult.getString("album_type"),
                    albumResult.getString("image"),
                    albumResult.getString("release_date"),
                    artist.getName(),
                    artist.getId()));
        }
        queryAlbums.closeQuery();
    }

    public static List<Artist> fetchArtists(int offset, int limit, String sortBy) throws SQLException {
        db = new SQLClient();

        Query query = db.query("SELECT * FROM artist ORDER BY " + sortBy +
                " LIMIT " + Integer.toString(offset) + "," + Integer.toString(limit));

        List<Artist> artists = new ArrayList<Artist>();
        ResultSet result = query.getResult();
        while (result.next()) {
            Artist artist = new Artist();
            setArtistAttrs(artist, result);
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
        setArtistAttrs(artist, result);

        query.closeQuery();
        db.closeConnection();
        return artist;
    }
}
