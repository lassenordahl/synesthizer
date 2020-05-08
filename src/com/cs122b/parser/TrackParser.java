package com.cs122b.parser;

import com.cs122b.client.SQLClient;
import com.cs122b.model.Album;
import com.cs122b.model.Artist;
import com.cs122b.model.Track;
import java.sql.PreparedStatement;

import com.mysql.jdbc.Statement;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.sql.SQLException;
import java.util.LinkedList;

class TrackParser extends BaseParser {
    LinkedList<Track> tracks;

    TrackParser() {
        super();
        this.tracks = new LinkedList<Track>();
    }

    private Track getTrack(Element trackElem) {

        Track track = new Track();

        // Set track attrs
        track.setId(this.getTextValue(trackElem, "id"));
        track.setName(this.getTextValue(trackElem, "name"));
        track.setTrack_number(this.getIntValue(trackElem, "track_number"));
        System.out.println("Track number :" + track.getId() + " : " + Integer.toString(track.getTrack_number()));

        // Handle Artists in track
        System.out.println("artists in " + track.getId());
        NodeList artistTags = trackElem.getElementsByTagName("artists");
        if (artistTags != null && artistTags.getLength() > 0) {
            NodeList artists = ((Element) artistTags.item(0)).getElementsByTagName("item");
            if (artists != null && artists.getLength() > 0) {
                for (int i = 0; i < artists.getLength(); i++) {
                    Element artistElem = (Element) artists.item(i);
                    Artist artist = new Artist();
                    artist.setId(this.getTextValue(artistElem, "id"));
                    System.out.println(this.getTextValue(artistElem, "id"));
                    track.addArtists(artist);
                }
            }
        }

        // NodeList artists = trackElem.getElementsByTagName("artists");
        // if (artists != null && artists.getLength() > 0) {
        // for (int i = 0; i < artists.getLength(); i++) {
        // Element artistElem = (Element) artists.item(i);
        // Artist artist = new Artist();
        // artist.setId(this.getTextValue(artistElem, "id"));
        // track.addArtists(artist);
        // }
        // }

        System.out.println("album in " + track.getId());
        NodeList albumsTags = trackElem.getElementsByTagName("album");
        if (albumsTags != null && albumsTags.getLength() > 0) {
            Element albumElem = (Element) albumsTags.item(0);
            Album album = new Album();
            album.setId(this.getTextValue(albumElem, "id"));
            System.out.println(this.getTextValue(albumElem, "id"));
            track.setAlbum(album);
        }

        // NodeList albums = ((Element)
        // albumsTags.item(0)).getElementsByTagName("item");
        // if (albums != null && albums.getLength() > 0) {
        // Element albumElem = (Element) albums.item(0);
        // Album album = new Album();
        // album.setId(this.getTextValue(albumElem, "id"));
        // System.out.println(this.getTextValue(albumElem, "id"));
        // track.setAlbum(album);
        // }

        return track;
    }

    void parseTracks(Element tracksElement) {
        NodeList nl = tracksElement.getElementsByTagName("item");
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {

                if (nl.item(i).getParentNode().getNodeName().equals("tracks")) {
                    // get the track element
                    Element el = (Element) nl.item(i);

                    // get the Track object
                    Track track = getTrack(el);

                    // add it to list
                    tracks.add(track);
                }
            }
        }
        return;
    }

    void commitTracks() throws SQLException {
        SQLClient db = new SQLClient();

        db.getConnection().setAutoCommit(false);

        String insertQuery = "INSERT INTO track(id, name, track_number) " + "VALUES(?,?,?);";
        PreparedStatement pstmt = db.getConnection().prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);

        String insertQuery2 = "INSERT INTO artist_in_track(artist_id, track_id) " + "VALUES(?,?);";
        PreparedStatement pstmt2 = db.getConnection().prepareStatement(insertQuery2, Statement.RETURN_GENERATED_KEYS);

        String insertQuery3 = "INSERT INTO track_in_album(track_id, album_id) " + "VALUES(?,?);";
        PreparedStatement pstmt3 = db.getConnection().prepareStatement(insertQuery3, Statement.RETURN_GENERATED_KEYS);

        for (Track track : tracks) {
            pstmt.setString(1, track.getId());
            pstmt.setString(2, track.getName());
            pstmt.setInt(3, track.getTrack_number());

            // artist_in_track
            if (track.getArtists() != null) {
                for (Artist artist : track.getArtists()) {
                    pstmt2.setString(1, artist.getId());
                    pstmt2.setString(2, track.getId());
                    pstmt2.addBatch();
                }
            }

            // track_in_album
            pstmt3.setString(1, track.getId());
            pstmt3.setString(2, track.getAlbum().getId());
            pstmt3.addBatch();

            pstmt.addBatch();
        }

        try {
            // Batch is ready, execute it to insert the data
            pstmt.executeBatch();
        } catch (SQLException e) {
            System.out.println("Error message: " + e.getMessage());
        }

        System.out.println("committed the track");

        try {
            // Batch is ready, execute it to insert the data
            pstmt2.executeBatch();
        } catch (SQLException e) {
            System.out.println("Error message: " + e.getMessage());
        }

        System.out.println("committed the artist_in_track");

        try {
            // Batch is ready, execute it to insert the data
            pstmt3.executeBatch();
        } catch (SQLException e) {
            System.out.println("Error message: " + e.getMessage());
        }

        db.getConnection().commit();
        System.out.println("committed the track_in_album");

        pstmt.close();
        pstmt2.close();
        pstmt3.close();
        db.closeConnection();
    }
}