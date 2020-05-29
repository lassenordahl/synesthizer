package com.cs122b.parser;

import com.cs122b.client.SQLClient;
import com.cs122b.model.Album;
import com.cs122b.model.Artist;
import com.cs122b.model.Track;

import java.sql.BatchUpdateException;
import java.sql.PreparedStatement;

import com.mysql.jdbc.Statement;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.naming.NamingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

class TrackParser extends BaseParser {
    int efficientFlag;
    int unfilteredCount;
    LinkedList<Track> tracks;

    TrackParser(int efficientFlag) {
        super();
        this.efficientFlag = efficientFlag;
        this.unfilteredCount = 0;
        this.tracks = new LinkedList<Track>();
    }

    private Track getTrack(Element trackElem) {

        Track track = new Track();

        // Set track attrs
        track.setId(this.getTextValue(trackElem, "id"));
        track.setName(this.getTextValue(trackElem, "name"));
        track.setTrack_number(this.getIntValue(trackElem, "track_number"));

        // Handle Artists in track
        NodeList artistTags = trackElem.getElementsByTagName("artists");
        if (artistTags != null && artistTags.getLength() > 0) {
            NodeList artists = ((Element) artistTags.item(0)).getElementsByTagName("item");
            if (artists != null && artists.getLength() > 0) {
                for (int i = 0; i < artists.getLength(); i++) {
                    Element artistElem = (Element) artists.item(i);
                    Artist artist = new Artist();
                    artist.setId(this.getTextValue(artistElem, "id"));
                    track.addArtists(artist);
                }
            }
        }

        NodeList albumsTags = trackElem.getElementsByTagName("album");
        if (albumsTags != null && albumsTags.getLength() > 0) {
            Element albumElem = (Element) albumsTags.item(0);
            Album album = new Album();
            album.setId(this.getTextValue(albumElem, "id"));
            track.setAlbum(album);
        }

        return track;
    }

    private Boolean hasAllData(Track track) {
        if (track.getId() != null && track.getName() != null && track.getTrack_number() != null) {
            return true;
        }
        this.addMissingData(track.toString());
        return false;
    }

    private Boolean isValid(Track track) {

        return hasAllData(track) && !this.isDuplicate(track.getId(), track.toString());
    }

    private void validationFilter() throws SQLException, NamingException {
        SQLClient db = new SQLClient();
        // get all ids in db and add to dupSet

        String query = "SELECT id FROM track;";
        PreparedStatement pstmt = db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        ResultSet result = pstmt.executeQuery();
        while (result.next()) {
            this.addToDupSet(result.getString("id"));
        }

        // copy all old tracks into new array
        LinkedList<Track> oldTracks = this.tracks;
        this.tracks = new LinkedList<Track>();

        // iterate over old tracks and add good
        for (Track track : oldTracks) {
            if (isValid(track)) {
                this.tracks.add(track);
            }
        }

        if (efficientFlag != 1) {
            this.tracks = oldTracks;
        }

        pstmt.close();
        db.closeConnection();
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

        unfilteredCount = tracks.size();
    }

    void commitTracks() throws SQLException, NamingException {
        SQLClient db = new SQLClient();

        db.getConnection().setAutoCommit(false);

        String insertQuery = "INSERT INTO track(id, name, track_number) " + "VALUES(?,?,?);";
        PreparedStatement pstmt = db.getConnection().prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);

        String insertQuery2 = "INSERT INTO artist_in_track(artist_id, track_id) " + "VALUES(?,?);";
        PreparedStatement pstmt2 = db.getConnection().prepareStatement(insertQuery2, Statement.RETURN_GENERATED_KEYS);

        String insertQuery3 = "INSERT INTO track_in_album(track_id, album_id) " + "VALUES(?,?);";
        PreparedStatement pstmt3 = db.getConnection().prepareStatement(insertQuery3, Statement.RETURN_GENERATED_KEYS);

        validationFilter();

        int artists_in_track = 0;
        for (Track track : tracks) {
            pstmt.setString(1, track.getId());
            pstmt.setString(2, track.getName());
            pstmt.setInt(3, (int) track.getTrack_number());

            // artist_in_track
            if (track.getArtists() != null) {
                for (Artist artist : track.getArtists()) {
                    pstmt2.setString(1, artist.getId());
                    pstmt2.setString(2, track.getId());
                    pstmt2.addBatch();
                    artists_in_track++;
                }
            }

            // track_in_album
            pstmt3.setString(1, track.getId());
            pstmt3.setString(2, track.getAlbum().getId());
            pstmt3.addBatch();

            pstmt.addBatch();
        }

        Integer inserts = null;
        try {
            // Batch is ready, execute it to insert the data
            pstmt.executeBatch();
        } catch (BatchUpdateException e) {
            inserts = this.getSuccessCount(e.getUpdateCounts());
            System.out.println(this.getFailCount(e.getUpdateCounts()));
        }

        try {
            // Batch is ready, execute it to insert the data
            pstmt2.executeBatch();
        } catch (BatchUpdateException e) {
        }

        try {
            // Batch is ready, execute it to insert the data
            pstmt3.executeBatch();
        } catch (BatchUpdateException e) {
        }

        db.getConnection().commit();

        if (inserts == null) {
            inserts = tracks.size();
        }

        // Print Report
        System.out.println(this.generateReport("Track", inserts, unfilteredCount));

        pstmt.close();
        pstmt2.close();
        pstmt3.close();
        db.closeConnection();
    }
}