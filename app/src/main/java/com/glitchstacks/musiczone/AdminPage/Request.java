package com.glitchstacks.musiczone.AdminPage;

public class Request {

    private String userID;

    public Request(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
