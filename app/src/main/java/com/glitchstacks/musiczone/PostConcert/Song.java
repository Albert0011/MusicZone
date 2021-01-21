package com.glitchstacks.musiczone.PostConcert;

import java.io.Serializable;

public class Song implements Serializable {

    private String song_name, song_link, artist_name, artist_link;

    public Song(String songName, String songLink, String artistName, String artistLink){
        this.song_name = songName;
        this.song_link = songLink;
        this.artist_name = artistName;
        this.artist_link = artistLink;
    }

    public String getSong_name(){return song_name;}
    public String getSong_link(){return song_link;}
    public String getArtist_name(){return artist_name;}
    public String getArtist_link(){return artist_link;}

}
