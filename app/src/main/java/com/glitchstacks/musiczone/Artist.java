package com.glitchstacks.musiczone;

public class Artist {

    private String name;
    private String imageURL;
    private String spotifyLink;

    public Artist(String name, String imageURL, String spotifyLink) {
        this.name = name;
        this.imageURL = imageURL;
        this.spotifyLink = spotifyLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getSpotifyLink() {
        return spotifyLink;
    }

    public void setSpotifyLink(String spotifyLink) {
        this.spotifyLink = spotifyLink;
    }
}
