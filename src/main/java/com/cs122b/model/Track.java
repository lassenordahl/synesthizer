package com.cs122b.model;

public class Track {

    private String id;
    private String name;
    private int track_number;

    public Track(String id, String name, int track_number) {
        this.id = id;
        this.name = name;
        this.track_number = track_number;
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

    public int getTrack_number() {
        return track_number;
    }

    public void setTrack_number(int track_number) {
        this.track_number = track_number;
    }
}
