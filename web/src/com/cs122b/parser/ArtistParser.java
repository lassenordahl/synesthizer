package com.cs122b.parser;

import com.cs122b.client.SQLClient;
import com.cs122b.model.Artist;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.naming.NamingException;
import java.sql.*;
import java.util.LinkedList;

class ArtistParser extends BaseParser {
    int efficientFlag;
    int unfilteredCount;
    LinkedList<Artist> artists;

    ArtistParser(int efficientFlag) {
        super();
        this.efficientFlag = efficientFlag;
        this.unfilteredCount = 0;
        this.artists = new LinkedList<Artist>();
    }

    private Artist getArtist(Element artistElem) {

        Artist artist = new Artist();

        // Set artist attrs
        artist.setId(getTextValue(artistElem, "id"));
        artist.setName(getTextValue(artistElem, "name"));

        // May need to verify this works
        NodeList imagesTag = artistElem.getElementsByTagName("images");
        if (imagesTag != null && imagesTag.getLength() > 0) {
            NodeList images = ((Element) imagesTag.item(0)).getElementsByTagName("item");
            if (images != null && images.getLength() > 0) {
                Element imageElem = (Element) images.item(0);
                artist.setImage(this.getTextValue(imageElem, "url"));
            }
        } else {
            artist.setImage("https://picsum.photos/200");
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

    private Boolean hasAllData(Artist artist) {
        if (artist.getId() != null && artist.getName() != null && artist.getImage() != null) {
            return true;
        }
        this.addMissingData(artist.toString());
        return false;
    }

    private Boolean isValid(Artist artist) {

        return hasAllData(artist) && !this.isDuplicate(artist.getId(), artist.toString());
    }

    private void validationFilter() throws SQLException, NamingException {
        SQLClient db = new SQLClient();
        // get all ids in db and add to dupSet

        String query = "SELECT id FROM artist;";
        PreparedStatement pstmt = db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        ResultSet result = pstmt.executeQuery();
        while (result.next()) {
            this.addToDupSet(result.getString("id"));
        }

        // copy all old artists into new array
        LinkedList<Artist> oldArtists = this.artists;
        this.artists = new LinkedList<Artist>();

        // iterate over old artists and add good
        for (Artist artist : oldArtists) {
            if (isValid(artist)) {
                this.artists.add(artist);
            }
        }

        if (efficientFlag != 1) {
            this.artists = oldArtists;
        }

        pstmt.close();
        db.closeConnection();
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

        unfilteredCount = artists.size();
    }

    void commitArtists() throws SQLException, NamingException {
        SQLClient db = new SQLClient(true);

        db.getConnection().setAutoCommit(false);

        String insertQuery = "INSERT INTO artist(id, name, image) " + "VALUES(?,?,?);";
        PreparedStatement pstmt = db.getConnection().prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);

        String insertQuery2 = "INSERT INTO artist_in_genre(artist_id, genre) " + "VALUES(?,?);";
        PreparedStatement pstmt2 = db.getConnection().prepareStatement(insertQuery2, Statement.RETURN_GENERATED_KEYS);

        validationFilter();

        int artists_in_genre = 0;
        for (Artist artist : artists) {
            pstmt.setString(1, artist.getId());
            pstmt.setString(2, artist.getName());
            pstmt.setString(3, artist.getImage());

            // Artist in genre
            if (artist.getGenres() != null) {
                for (String genre : artist.getGenres()) {
                    pstmt2.setString(1, artist.getId());
                    pstmt2.setString(2, genre);
                    pstmt2.addBatch();
                    artists_in_genre++;
                }
            }

            pstmt.addBatch();
        }

        Integer inserts = null;
        try {
            pstmt.executeBatch();
        } catch (BatchUpdateException e) {
            inserts = this.getSuccessCount(e.getUpdateCounts());
        }

        // catch (SQLException e2) {
        // e2.printStackTrace();
        // System.out.println(e2.getMessage());
        // }

        try {
            pstmt2.executeBatch();
        } catch (BatchUpdateException e) {
        }
        db.getConnection().commit();

        if (inserts == null) {
            inserts = artists.size();
        }

        // Print Report
        System.out.println(this.generateReport("Artist", inserts, unfilteredCount));

        pstmt.close();
        pstmt2.close();
        db.closeConnection();
    }
}