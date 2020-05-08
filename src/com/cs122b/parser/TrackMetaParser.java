package com.cs122b.parser;

import com.cs122b.model.TrackMeta;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
                // get the trackMeta element
                Element el = (Element) nl.item(i);

                // get the TrackMeta object
                TrackMeta trackMeta = getTrackMeta(el);

                // add it to list
                trackMetas.add(trackMeta);
            }
        }
        return;
    }

    void commitTrackMetas() {
        return;
    }
}