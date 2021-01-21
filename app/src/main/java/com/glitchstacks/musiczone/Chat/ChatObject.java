package com.glitchstacks.musiczone.Chat;

public class ChatObject {
    private String message, username;
    private Boolean currentUser;
    public ChatObject(String message, Boolean currentUser, String username){
        this.message = message;
        this.username = username;
        this.currentUser = currentUser;
    }

    public String getMessage(){
        return message;
    }
    public void setMessage(String userID){
        this.message = message;
    }

    public Boolean getCurrentUser(){
        return currentUser;
    }
    public void setCurrentUser(Boolean currentUser){
        this.currentUser = currentUser;
    }

    public String getUsername(){
        return username;
    }
    public void setUsername(String userID){
        this.username = username;
    }

}