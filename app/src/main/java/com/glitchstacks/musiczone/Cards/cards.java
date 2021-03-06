package com.glitchstacks.musiczone.Cards;

public class cards {

    private String userId;
    private String name;
    private String profileImageUrl;
    private String description;
    private String date;
    public cards (String userId, String name, String profileImageUrl, String description, String date){
        this.userId = userId;
        this.name = name +", "+date;
        this.profileImageUrl = profileImageUrl;
        this.description = description;
        this.date = date;
    }

    public String getUserId(){
        return userId;
    }
    public void setUserID(String userID){
        this.userId = userId;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getProfileImageUrl(){
        return profileImageUrl;
    }
    public void setProfileImageUrl(String profileImageUrl){
        this.profileImageUrl = profileImageUrl;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAge() {
        return date;
    }

    public void setAge(String age) {
        this.date = age;
    }
}