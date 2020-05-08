package com.cs122b.model;

public class DatabaseMeta {

    private int track_count;
    private int album_count;
    private int artist_count;

    public DatabaseMeta() {

    }

    public int getAlbum_count() {
        return album_count;
    }

    public int getArtist_count() {
        return artist_count;
    }

    public int getTrack_count() {
        return track_count;
    }

    public void setTrack_count(int track_count) {
        this.track_count = track_count;
    }

    public void setAlbum_count(int album_count) {
        this.album_count = album_count;
    }

    public void setArtist_count(int artist_count) {
        this.artist_count = artist_count;
    }
}
