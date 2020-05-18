package edu.uci.ics.fabflixmobile;

import java.util.ArrayList;

public class SongsResponse {

    ArrayList<Track> songs = new ArrayList<>();

    public SongsResponse() {

    }

    public void setSongs(ArrayList<Track> songs) {
        this.songs = songs;
    }

    public ArrayList<Track> getSongs() {
        return songs;
    }
}
