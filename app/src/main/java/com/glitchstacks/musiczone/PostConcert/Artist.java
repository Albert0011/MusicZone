package com.glitchstacks.musiczone.PostConcert;

import java.io.Serializable;

public class Artist implements Serializable {

    private String name;
    private String imageURL;
    private String spotifyLink;
    private String id;
    Boolean isSelected;

    public Artist(String name, String imageURL, String spotifyLink, String id) {
        this.name = name;
        this.imageURL = imageURL;
        this.spotifyLink = spotifyLink;
        this.isSelected = false;
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
