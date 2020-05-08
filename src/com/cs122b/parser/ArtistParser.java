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

        NodeList images = artistElem.getElementsByTagName("images");
        if (images != null && images.getLength() > 0) {
            Element image = (Element) images.item(0);
            artist.setImage(this.getTextValue(image, "url"));
        } else {
            artist.setImage(getTextValue(artistElem, "https://picsum.photos/200"));
        }

        NodeList genres = artistElem.getElementsByTagName("genres");
        if (genres != null && genres.getLength() > 0) {
            for (int i = 0; i < genres.getLength(); i++) {
                Element genreElem = (Element) genres.item(i);
                artist.addGenre(this.getTextValue(genreElem, "item"));
            }
        }

        return artist;
    }

    void parseArtists(Element artistsElement) {
        NodeList nl = artistsElement.getElementsByTagName("item");
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {
                // get the artist element
                Element el = (Element) nl.item(i);

                // get the Artist object
                Artist artist = getArtist(el);

                // add it to list
                artists.add(artist);
            }
        }
        return;
    }

    void commitArtists() throws SQLException {
        SQLClient db = new SQLClient();

        db.getConnection().setAutoCommit(false);

        String insertQuery = "INSERT INTO artist(id, name, image) " + "VALUES(?,?,?);";
        PreparedStatement pstmt = db.getConnection().prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);

        for (Artist artist : artists) {
            pstmt.setString(1, artist.getId());
            pstmt.setString(2, artist.getName());
            pstmt.setString(3, artist.getImage());
            pstmt.addBatch();
        }

        try {
            // Batch is ready, execute it to insert the data
            pstmt.executeBatch();
        } catch (SQLException e) {
            System.out.println("Error message: " + e.getMessage());
        }

        db.getConnection().commit();
        System.out.println("we have committed the artists");

        pstmt.close();
        db.closeConnection();
    }
}