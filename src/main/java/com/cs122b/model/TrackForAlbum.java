package com.cs122b.model;

public class TrackForAlbum {

    private String id;
    private String name;
    private int duration_ms;

    public TrackForAlbum(String id, String name, int duration_ms) {
        this.id = id;
        this.name = name;
        this.duration_ms = duration_ms;
    }

    public int getDuration_ms() {
        return duration_ms;
    }

    public void setDuration_ms(int duration_ms) {
        this.duration_ms = duration_ms;
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
}
