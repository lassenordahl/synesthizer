package com.cs122b.parser;

import com.cs122b.client.SQLClient;
import com.cs122b.model.Artist;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

class ArtistParser extends BaseParser {
    LinkedList<Artist> artists;

    ArtistParser() {
        super();
        this.artists = new LinkedList<Artist>();
    }

    private Artist getArtist(Element artistElem) {

        Artist artist = new Artist();

        // Set artist attrs
        artist.setId(getTextValue(artistElem, "id"));
        artist.setName(getTextValue(artistElem, "name"));

        // May need to verify this works
        NodeList images = artistElem.getElementsByTagName("images");
        if (images != null && images.getLength() > 0) {
            Element image = (Element) images.item(0);
            artist.setImage(this.getTextValue(image, "url"));
        } else {
            artist.setImage(getTextValue(artistElem, "https://picsum.photos/200"));
        }

        NodeList genreTags = artistElem.getElementsByTagName("genres");
        if (genreTags != null && genreTags.getLength() > 0) {
            NodeList genres = ((Element) genreTags.item(0)).getElementsByTagName("item");
            if (genres != null && genres.getLength() > 0)
                for (int i = 0; i < genres.getLength(); i++) {
                    Element genreElem = (Element) genres.item(i);
                    artist.addGenre(genreElem.getFirstChild().getNodeValue());
                }
        }

        return artist;
    }

    void parseArtists(Element artistsElement) {
        NodeList nl = artistsElement.getElementsByTagName("item");
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {
                // get the artist element
                if (nl.item(i).getParentNode().getNodeName().equals("artists")) {
                    Element el = (Element) nl.item(i);

                    // get the Artist object
                    Artist artist = getArtist(el);

                    // add it to list
                    artists.add(artist);
                }
            }
        }
        return;
    }

    void commitArtists() throws SQLException {
        SQLClient db = new SQLClient();

        db.getConnection().setAutoCommit(false);

        String insertQuery = "INSERT INTO artist(id, name, image) " + "VALUES(?,?,?);";
        PreparedStatement pstmt = db.getConnection().prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);

        String insertQuery2 = "INSERT INTO artist_in_genre(artist_id, genre) " + "VALUES(?,?);";
        PreparedStatement pstmt2 = db.getConnection().prepareStatement(insertQuery2, Statement.RETURN_GENERATED_KEYS);

        for (Artist artist : artists) {
            pstmt.setString(1, artist.getId());
            pstmt.setString(2, artist.getName());
            pstmt.setString(3, artist.getImage());

            // Artist in genre
            if (artist.getGenres() != null) {
                for (String genre : artist.getGenres()) {
                    System.out.println(genre);
                    pstmt2.setString(1, artist.getId());
                    pstmt2.setString(2, genre);
                    pstmt2.addBatch();
                }
            }

            pstmt.addBatch();
        }

        try {
            pstmt.executeBatch();
        } catch (SQLException e) {
            System.out.println("Error message: " + e.getMessage());
        }

        System.out.println("committed to artist table");

        try {
            pstmt2.executeBatch();
        } catch (SQLException e) {
            System.out.println("Error message: " + e.getMessage());
        }

        db.getConnection().commit();

        System.out.println("committed to artist_in_genre table");

        pstmt.close();
        pstmt2.close();
        db.closeConnection();
    }
}