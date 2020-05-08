package com.cs122b.parser;

import com.cs122b.client.SQLClient;
import com.cs122b.model.TrackMeta;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

class TrackMetaParser extends BaseParser {
    LinkedList<TrackMeta> trackMetas;

    TrackMetaParser() {
        super();
        this.trackMetas = new LinkedList<TrackMeta>();
    }

    private TrackMeta getTrackMeta(Element trackMetaElem) {

        TrackMeta trackMeta = new TrackMeta();

        // Set trackMeta attrs
        trackMeta.setId(this.getTextValue(trackMetaElem, "id"));
        trackMeta.setAcousticness(this.getFloatValue(trackMetaElem, "acousticness"));
        trackMeta.setAnalysis_url(this.getTextValue(trackMetaElem, "analysis_url"));
        trackMeta.setDanceability(this.getFloatValue(trackMetaElem, "danceability"));
        trackMeta.setDuration_ms(this.getIntValue(trackMetaElem, "duration_ms"));
        trackMeta.setEnergy(this.getFloatValue(trackMetaElem, "energy"));
        trackMeta.setInstrumentalness(this.getFloatValue(trackMetaElem, "instrumentalness"));
        trackMeta.setNote(this.getIntValue(trackMetaElem, "note"));
        trackMeta.setLiveness(this.getFloatValue(trackMetaElem, "liveness"));
        trackMeta.setLoudness(this.getFloatValue(trackMetaElem, "loudness"));
        trackMeta.setMode(this.getIntValue(trackMetaElem, "mode"));
        trackMeta.setSpeechiness(this.getFloatValue(trackMetaElem, "speechiness"));
        trackMeta.setTempo(this.getFloatValue(trackMetaElem, "tempo"));
        trackMeta.setTime_signature(this.getIntValue(trackMetaElem, "time_signature"));
        trackMeta.setTrack_href(this.getTextValue(trackMetaElem, "track_href"));
        trackMeta.setType(this.getTextValue(trackMetaElem, "type"));
        trackMeta.setValence(this.getFloatValue(trackMetaElem, "valence"));

        return trackMeta;
    }

    void parseTrackMetas(Element trackMetasElement) {
        NodeList nl = trackMetasElement.getElementsByTagName("item");
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getParentNode().getNodeName().equals("track_metas")) {
                    // get the trackMeta element
                    Element el = (Element) nl.item(i);

                    // get the TrackMeta object
                    TrackMeta trackMeta = getTrackMeta(el);

                    // add it to list
                    trackMetas.add(trackMeta);
                }
            }
        }
        return;
    }

    void commitTrackMetas() throws SQLException {
        SQLClient db = new SQLClient();

        db.getConnection().setAutoCommit(false);

        String insertQuery = "INSERT INTO track_meta(id,acousticness,analysis_url,danceability,duration_ms,energy,instrumentalness,note,liveness,loudness,mode,speechiness,tempo,time_signature,track_href,type,uri,valence) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        PreparedStatement pstmt = db.getConnection().prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);

        for (TrackMeta trackMeta : trackMetas) {
            pstmt.setString(1, trackMeta.getId());
            pstmt.setFloat(2, trackMeta.getAcousticness());
            pstmt.setString(3, trackMeta.getAnalysis_url());
            pstmt.setFloat(4, trackMeta.getDanceability());
            pstmt.setInt(5, trackMeta.getDuration_ms());
            pstmt.setFloat(6, trackMeta.getEnergy());
            pstmt.setFloat(7, trackMeta.getInstrumentalness());
            pstmt.setInt(8, trackMeta.getNote());
            pstmt.setFloat(9, trackMeta.getLiveness());
            pstmt.setFloat(10, trackMeta.getLoudness());
            pstmt.setInt(11, trackMeta.getMode());
            pstmt.setFloat(12, trackMeta.getSpeechiness());
            pstmt.setFloat(13, trackMeta.getTempo());
            pstmt.setInt(14, trackMeta.getTime_signature());
            pstmt.setString(15, trackMeta.getTrack_href());
            pstmt.setString(16, trackMeta.getType());
            pstmt.setString(17, trackMeta.getUri());
            pstmt.setFloat(18, trackMeta.getValence());

            pstmt.addBatch();
        }

        try {
            pstmt.executeBatch();
        } catch (SQLException e) {
            System.out.println("Error message: " + e.getMessage());
        }

        db.getConnection().commit();
        System.out.println("committed to track_meta table");

        pstmt.close();
        db.closeConnection();
    }
}