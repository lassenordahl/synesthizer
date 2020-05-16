package com.cs122b.parser;

import com.cs122b.client.SQLClient;
import com.cs122b.model.TrackMeta;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.sql.*;
import java.util.LinkedList;

class TrackMetaParser extends BaseParser {
    int efficientFlag;
    int unfilteredCount;
    LinkedList<TrackMeta> trackMetas;

    TrackMetaParser(int efficientFlag) {
        super();
        this.efficientFlag = efficientFlag;
        this.unfilteredCount = 0;
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

    private Boolean hasAllData(TrackMeta trackMeta) {
        if (trackMeta.getId() != null && trackMeta.getDuration_ms() != null) {
            return true;
        }
        this.addMissingData(trackMeta.toString());
        return false;
    }

    private Boolean isValid(TrackMeta trackMeta) {

        return hasAllData(trackMeta) && !this.isDuplicate(trackMeta.getId(), trackMeta.toString());
    }

    private void validationFilter() throws SQLException {
        SQLClient db = new SQLClient();
        // get all ids in db and add to dupSet

        String query = "SELECT id FROM track_meta;";
        PreparedStatement pstmt = db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        ResultSet result = pstmt.executeQuery();
        while (result.next()) {
            this.addToDupSet(result.getString("id"));
        }

        // copy all old trackMetas into new array
        LinkedList<TrackMeta> oldTrackMetas = this.trackMetas;
        this.trackMetas = new LinkedList<TrackMeta>();

        // iterate over old trackMetas and add good
        for (TrackMeta trackMeta : oldTrackMetas) {
            if (isValid(trackMeta)) {
                this.trackMetas.add(trackMeta);
            }
        }

        if (efficientFlag != 1) {
            this.trackMetas = oldTrackMetas;
        }

        pstmt.close();
        db.closeConnection();
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

        unfilteredCount = trackMetas.size();
    }

    void commitTrackMetas() throws SQLException {
        SQLClient db = new SQLClient();

        db.getConnection().setAutoCommit(false);

        String insertQuery = "INSERT INTO track_meta(id,acousticness,analysis_url,danceability,duration_ms,energy,instrumentalness,note,liveness,loudness,mode,speechiness,tempo,time_signature,track_href,type,uri,valence) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        PreparedStatement pstmt = db.getConnection().prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);

        validationFilter();

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

        Integer inserts = null;
        try {
            pstmt.executeBatch();
        } catch (BatchUpdateException e) {
            inserts = this.getSuccessCount(e.getUpdateCounts());
        }

        db.getConnection().commit();

        if (inserts == null) {
            inserts = trackMetas.size();
        }

        // Print Report
        System.out.println(this.generateReport("TrackMeta", inserts, unfilteredCount));

        pstmt.close();
        db.closeConnection();
    }
}