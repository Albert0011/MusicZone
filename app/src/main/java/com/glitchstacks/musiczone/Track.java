package com.glitchstacks.musiczone;

public class Track {

    private String spotifyLink;
    private String name;

    public Track(String spotifyLink, String name) {
        this.spotifyLink = spotifyLink;
        this.name = name;
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
}
