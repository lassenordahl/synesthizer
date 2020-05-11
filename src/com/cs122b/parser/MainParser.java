package com.cs122b.parser;

import java.io.IOException;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MainParser {
    String fileName;
    Document dom;

    ArtistParser artistParser;
    AlbumParser albumParser;
    TrackParser trackParser;
    TrackMetaParser trackMetaParser;

    public MainParser(String file) {
        fileName = file;
        artistParser = new ArtistParser(1);
        albumParser = new AlbumParser(1);
        trackParser = new TrackParser(1);
        trackMetaParser = new TrackMetaParser(1);
    }

    public void run() {
        // parse the xml file and get the dom object
        parseXmlFile();

        System.out.println("we are going to parse the Doc");
        // get each employee element and create a Employee object
        parseDocument();
    }

    private void parseXmlFile() {
        // get the factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse(fileName);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void parseDocument() {
        // get the root elememt
        Element docEle = dom.getDocumentElement();

        NodeList artists = docEle.getElementsByTagName("artists");
        if (artists != null && artists.getLength() > 0) {
            System.out.println("Found artists");
            // get artists element
            Element artistsElem = (Element) artists.item(0);
            artistParser.parseArtists(artistsElem);
            try {
                artistParser.commitArtists();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        NodeList albums = docEle.getElementsByTagName("albums");
        if (albums != null && albums.getLength() > 0) {
            System.out.println("Found albums");
            // get artists element
            Element albumsElem = (Element) albums.item(0);
            albumParser.parseAlbums(albumsElem);
            try {
                albumParser.commitAlbums();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        NodeList tracks = docEle.getElementsByTagName("tracks");
        if (tracks != null && tracks.getLength() > 0) {
            System.out.println("Found tracks");
            // get artists element
            Element tracksElem = (Element) tracks.item(0);
            trackParser.parseTracks(tracksElem);
            try {
                trackParser.commitTracks();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        NodeList tracks_meta = docEle.getElementsByTagName("track_metas");
        if (tracks_meta != null && tracks_meta.getLength() > 0) {
            System.out.println("Found track_meta");
            // get artists element
            Element tracksMetaElem = (Element) tracks_meta.item(0);
            trackMetaParser.parseTrackMetas(tracksMetaElem);
            try {
                trackMetaParser.commitTrackMetas();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting Parser");
        MainParser dpe = new MainParser(args[0]);
        dpe.run();
    }
}