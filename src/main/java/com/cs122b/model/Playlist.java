package com.cs122b.model;

import java.util.ArrayList;

public class Playlist {

    private int id;
    private String name;
    private int user_id;
    private String image;
    private String creation_date;
    private ArrayList<Track> tracks;

    public Playlist() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreation_date() {
        return this.creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public ArrayList<Track> getTracks() {
        return this.tracks;
    }

    public void setTracks(ArrayList<Track> tracks) {
        this.tracks = tracks;
    }

    public void addTrack(Track track) {
        this.tracks.add(track);
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}

