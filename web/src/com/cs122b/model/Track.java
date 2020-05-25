package com.cs122b.model;

import java.util.ArrayList;
import com.cs122b.model.Album;
import com.cs122b.model.Artist;

public class Track {
    // Track
    private String id;
    private String name;
    private Integer track_number;

    // Track-Meta-Light
    private Integer duration_ms;
    private Integer popularity;

    // Track_to_artist
    private ArrayList<Artist> artists;

    // Track_to_album
    private Album album;

    public Track() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTrack_number() {
        return track_number;
    }

    public void setTrack_number(int track_number) {
        this.track_number = track_number;
    }

    public Integer getDuration_ms() {
        return duration_ms;
    }

    public void setDuration_ms(int duration_ms) {
        this.duration_ms = duration_ms;
    }

    public ArrayList<Artist> getArtists() {
        return artists;
    }

    public void setArtists(ArrayList<Artist> artists) {
        this.artists = artists;
    }

    public void addArtists(Artist artist) {
        if (this.artists == null) {
            this.artists = new ArrayList<Artist>();
        }
        this.artists.add(artist);
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String toString() {
        return String.format("-Track- id: %s name: %s track_number: %d", getId(), getName(), getTrack_number());
    }
}
