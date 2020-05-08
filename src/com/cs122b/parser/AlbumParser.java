package com.cs122b.parser;

import com.cs122b.client.SQLClient;
import com.cs122b.model.Album;
import com.cs122b.model.Artist;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.sql.BatchUpdateException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

class AlbumParser extends BaseParser {
    LinkedList<Album> albums;

    AlbumParser() {
        super();
        this.albums = new LinkedList<Album>();
    }

    private Album getAlbum(Element albumElem) {

        Album album = new Album();

        // Set album attrs
        album.setId(this.getTextValue(albumElem, "id"));
        album.setName(this.getTextValue(albumElem, "name"));
        album.setAlbum_type(this.getTextValue(albumElem, "album_type"));
        album.setRelease_date(this.getTextValue(albumElem, "release_date"));

        NodeList imagesTag = albumElem.getElementsByTagName("images");
        if (imagesTag != null && imagesTag.getLength() > 0) {
            NodeList images = ((Element) imagesTag.item(0)).getElementsByTagName("item");
            if (images != null && images.getLength() > 0) {
                Element imageElem = (Element) images.item(0);
                album.setImage(this.getTextValue(imageElem, "url"));
            }
        } else {
            album.setImage("https://picsum.photos/200");
        }

        // Handle Artists and Album
        NodeList artistTags = albumElem.getElementsByTagName("artists");
        if (artistTags != null && artistTags.getLength() > 0) {
            NodeList artists = ((Element) artistTags.item(0)).getElementsByTagName("item");
            if (artists != null && artists.getLength() > 0) {
                for (int i = 0; i < artists.getLength(); i++) {
                    Element artistElem = (Element) artists.item(i);
                    Artist artist = new Artist();
                    artist.setId(this.getTextValue(artistElem, "id"));
                    album.addArtists(artist);
                }
            }
        }

        // NodeList genreTags = artistElem.getElementsByTagName("genres");
        // if (genreTags != null && genreTags.getLength() > 0) {
        // NodeList genres = ((Element) genreTags.item(0)).getElementsByTagName("item");
        // if (genres != null && genres.getLength() > 0)
        // for (int i = 0; i < genres.getLength(); i++) {
        // Element genreElem = (Element) genres.item(i);
        // artist.addGenre(genreElem.getFirstChild().getNodeValue());
        // }
        // }

        return album;
    }

    void parseAlbums(Element albumsElement) {
        NodeList nl = albumsElement.getElementsByTagName("item");
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getParentNode().getNodeName().equals("albums")) {
                    // get the album element
                    Element el = (Element) nl.item(i);

                    // get the Album object
                    Album album = getAlbum(el);

                    // add it to list
                    albums.add(album);
                }
            }
        }
        return;
    }

    void commitAlbums() throws SQLException {
        SQLClient db = new SQLClient();

        db.getConnection().setAutoCommit(false);

        String insertQuery = "INSERT INTO album(id, name, album_type, image, release_date) " + "VALUES(?,?,?,?,?);";
        PreparedStatement pstmt = db.getConnection().prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);

        String insertQuery2 = "INSERT INTO artist_in_album(artist_id, album_id) " + "VALUES(?,?);";
        PreparedStatement pstmt2 = db.getConnection().prepareStatement(insertQuery2, Statement.RETURN_GENERATED_KEYS);
        int artists_in_albums = 0;

        for (Album album : albums) {
            pstmt.setString(1, album.getId());
            pstmt.setString(2, album.getName());
            pstmt.setString(3, album.getAlbum_type());
            pstmt.setString(4, album.getImage());
            pstmt.setString(5, album.getRelease_date());

            // artist_in_album
            if (album.getArtists() != null) {
                for (Artist artist : album.getArtists()) {
                    pstmt2.setString(1, artist.getId());
                    pstmt2.setString(2, album.getId());
                    pstmt2.addBatch();
                    artists_in_albums++;
                }
            }

            pstmt.addBatch();
        }

        try {
            // Batch is ready, execute it to insert the data
            pstmt.executeBatch();
        } catch (BatchUpdateException e) {
            System.out.println(String.format("Batch was able to insert %d out of %d albums.",
                    this.getSuccessCount(e.getUpdateCounts()), albums.size()));
        }

        System.out.println("committed the artist");

        try {
            // Batch is ready, execute it to insert the data
            pstmt2.executeBatch();
        } catch (BatchUpdateException e) {
            System.out.println(String.format("Batch was able to insert %d out of %d artists_in_albums.",
                    this.getSuccessCount(e.getUpdateCounts()), artists_in_albums));
        }

        db.getConnection().commit();
        System.out.println("committed the artist_in_album");

        pstmt.close();
        pstmt2.close();
        db.closeConnection();
    }
}