package com.glitchstacks.musiczone.PostConcert;

import java.io.Serializable;
import java.util.List;

public class Performance implements Serializable {

    private Artist artist;
    private List<Track> trackList;

    public Performance(Artist artist, List<Track> trackList) {
        this.artist = artist;
        this.trackList = trackList;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public List<Track> getTrackList() {
        return trackList;
    }

    public void setTrackList(List<Track> trackList) {
        this.trackList = trackList;
    }
}
