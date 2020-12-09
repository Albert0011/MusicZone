package com.glitchstacks.musiczone.HelperClasses.ExplorePageAdapter;

public class FeaturedHelperClass {

    int image;
    String title, passage;

    public FeaturedHelperClass(int image, String title, String passage) {
        this.image = image;
        this.title = title;
        this.passage = passage;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getPassage() {
        return passage;
    }
}
