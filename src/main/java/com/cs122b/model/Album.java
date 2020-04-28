package com.cs122b.model;

import java.util.ArrayList;

public class Album {

    private String id;
    private String name;
    private String album_type;
    private String image;
    private String release_date;
    private int popularity;

    // artists_in_album
    private ArrayList<Artist> artists;

    public Album() {
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

    public String getAlbum_type() {
        return album_type;
    }

    public void setAlbum_type(String album_type) {
        this.album_type = album_type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public ArrayList<Artist> getArtists() {
        return this.artists;
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
}
