package com.cs122b.model;

import java.util.ArrayList;

// Even though these aren't "required", I'm getting weird errors trying to call the add_track function elsewhere
import com.cs122b.model.Track;
import com.cs122b.model.Album;

public class Playlist {

    // Playlist
    private int id;
    private String name;
    private int user_id;
    private String image;
    private String creation_date;
    private ArrayList<Track> tracks;
    private ArrayList<Album> albums;
    private int playlistsCreated;

    public Playlist() {
        tracks = new ArrayList<Track>();
        albums = new ArrayList<Album>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
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

    public void removeTrack(String id) {
        for (int i = 0; i < tracks.size(); i++) {
            if (tracks.get(i).getId().equals(id)) {
                tracks.remove(i);
                break;
            }
        }
    }

    public void addAlbum(Album album) {
        this.albums.add(album);
    }

    public void removeAlbum(String id) {
        for (int i = 0; i < albums.size(); i++) {
            if (albums.get(i).getId().equals(id)) {
                albums.remove(i);
                break;
            }
        }
    }

    public void setPlaylistsCreated(int playlistsCreated) {
        this.playlistsCreated = playlistsCreated;
    }

    public int getPlaylistsCreated() {
        return playlistsCreated;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}

