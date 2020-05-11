package com.cs122b.model;

import java.util.ArrayList;
import com.cs122b.model.Album;

public class Artist {
    private String id;
    private String name;
    private String image;
    private int popularity;
    private ArrayList<String> genres;

    // Artist in album
    private ArrayList<Album> albums;

    public Artist() {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public void addGenre(String genre) {
        if (this.genres == null) {
            this.genres = new ArrayList<String>();
        }
        this.genres.add(genre);
    }

    public void setAlbums(ArrayList<Album> albums) {
        this.albums = albums;
    }

    public void addAlbum(Album album) {
        if (this.albums == null) {
            this.albums = new ArrayList<Album>();
        }
        this.albums.add(album);
    }

    public int getPopularity() {
        return this.popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String toString() {
        return String.format("-Artist- id: %s name: %s image: %s", getId(), getName(), getImage());
    }
}
