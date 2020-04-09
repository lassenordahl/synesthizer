package com.cs122b.model;

public class Album {

    private String id;
    private String name;
    private String album_type;
    private String image;
    private String release_date;
    private String artist_name;
    private String artist_id;

    public Album(String id, String name, String album_type, String image, String release_date, String artist_name, String artist_id) {
        this.id = id;
        this.name = name;
        this.album_type = album_type;
        this.image = image;
        this.release_date = release_date;
        this.artist_name = artist_name;
        this.artist_id = artist_id;
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

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public String getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(String artist_id) {
        this.artist_id = artist_id;
    }
}
