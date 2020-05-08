package com.cs122b.parser;

import com.cs122b.model.Album;
import com.cs122b.model.Artist;
import com.cs122b.model.Track;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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

        // Handle Artists and Albums
        NodeList artists = trackElem.getElementsByTagName("artists");
        if (artists != null && artists.getLength() > 0) {
            for (int i = 0; i < artists.getLength(); i++) {
                Element artistElem = (Element) artists.item(i);
                Artist artist = new Artist();
                artist.setId(this.getTextValue(artistElem, "id"));
                track.addArtists(artist);
            }
        }

        NodeList albums = trackElem.getElementsByTagName("albums");
        if (albums != null && albums.getLength() > 0) {
            Element albumElem = (Element) albums.item(0);
            Album album = new Album();
            album.setId(this.getTextValue(albumElem, "id"));
            track.setAlbum(album);
        }

        return track;
    }

    void parseTracks(Element tracksElement) {
        NodeList nl = tracksElement.getElementsByTagName("item");
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {
                // get the track element
                Element el = (Element) nl.item(i);

                // get the Track object
                Track track = getTrack(el);

                // add it to list
                tracks.add(track);
            }
        }
        return;
    }

    void commitTracks() {
        // Handle adding the artist_in_track
        // Handle adding the artist_in_album
        // Handle track_in_album
        return;
    }
}