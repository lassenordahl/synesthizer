package com.cs122b.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Track {
    // Track
    private String id;
    private String name;
    private int track_number;
    // Track-Meta
    private float acousticness;
    private String analysis_url;
    private float danceability;
    private int duration_ms;
    private float energy;
    private float instrumentalness;
    private int note;
    private float liveness;
    private float loudness;
    private int mode;
    private float speechiness;
    private float tempo;
    private int time_signature;
    private String track_href;
    private String type;
    private float valence;

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

    public int getTrack_number() {
        return track_number;
    }

    public void setTrack_number(int track_number) {
        this.track_number = track_number;
    }

    public float getAcousticness() {
        return acousticness;
    }

    public void setAcousticness(float acousticness) {
        this.acousticness = acousticness;
    }

    public String getAnalysis_url() {
        return analysis_url;
    }

    public void setAnalysis_url(String analysis_url) {
        this.analysis_url = analysis_url;
    }

    public float getDanceability() {
        return danceability;
    }

    public void setDanceability(float danceability) {
        this.danceability = danceability;
    }

    public int getDuration_ms() {
        return duration_ms;
    }

    public void setDuration_ms(int duration_ms) {
        this.duration_ms = duration_ms;
    }

    public float getEnergy() {
        return energy;
    }

    public void setEnergy(float energy) {
        this.energy = energy;
    }

    public float getInstrumentalness() {
        return instrumentalness;
    }

    public void setInstrumentalness(float instrumentalness) {
        this.instrumentalness = instrumentalness;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public float getLiveness() {
        return liveness;
    }

    public void setLiveness(float liveness) {
        this.liveness = liveness;
    }

    public float getLoudness() {
        return loudness;
    }

    public void setLoudness(float loudness) {
        this.loudness = loudness;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public float getSpeechiness() {
        return speechiness;
    }

    public void setSpeechiness(float speechiness) {
        this.speechiness = speechiness;
    }

    public float getTempo() {
        return tempo;
    }

    public void setTempo(float tempo) {
        this.tempo = tempo;
    }

    public int getTime_signature() {
        return time_signature;
    }

    public void setTime_signature(int time_signature) {
        this.time_signature = time_signature;
    }

    public String getTrack_href() {
        return track_href;
    }

    public void setTrack_href(String track_href) {
        this.track_href = track_href;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getValence() {
        return valence;
    }

    public void setValence(float valence) {
        this.valence = valence;
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
}
