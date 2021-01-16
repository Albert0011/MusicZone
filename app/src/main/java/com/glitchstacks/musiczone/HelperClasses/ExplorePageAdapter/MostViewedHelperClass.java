package com.glitchstacks.musiczone.HelperClasses.ExplorePageAdapter;

public class MostViewedHelperClass {
    String image,title, passage,date;

    public MostViewedHelperClass(String image, String title, String passage, String date) {
        this.image = image;
        this.title = title;
        this.passage = passage;
        this.date = date;
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

}
