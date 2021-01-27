package com.glitchstacks.musiczone.PostConcert;

import java.io.Serializable;

public class Track implements Serializable {

    private String spotifyLink;
    private String name;
    private String id;
    Boolean isSelected;

    public Track(String spotifyLink, String name, String id) {
        this.spotifyLink = spotifyLink;
        this.name = name;
        this.id = id;
        this.isSelected = false;
    }

    public String getSpotifyLink() {
        return spotifyLink;
    }

    public void setSpotifyLink(String spotifyLink) {
        this.spotifyLink = spotifyLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
