package com.glitchstacks.musiczone.HelperClasses.ExplorePageAdapter;

public class MostViewedHelperClass {

    private String image,title, passage,date, key, genre;

    public MostViewedHelperClass(String image, String title, String passage, String date, String key, String genre) {
        this.image = image;
        this.title = title;
        this.passage = passage;
        this.date = date;
        this.key = key;
        this.genre = genre;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getPassage() {
        return passage;
    }

    public String getDate(){return date;}

    public String getKey(){return key;}

    public String getGenre(){return genre;}

}
