package com.glitchstacks.musiczone.Database;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    //Variable
    SharedPreferences usersSession;
    SharedPreferences.Editor editor;
    Context context;

    //SessionNames
    public static final String SESSION_USERSESSION = "userLoginSession";
    public static final String SESSION_REMEMBERME = "rememberMe";
    public static final String SESSION_SPOTIFY = "spotifyKey";
    private static final String IS_LOGIN = "IsLoggedIn";
    // user session variables
    public static final String KEY_FULLNAME = "fullname";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONENUMBER = "phoneNo";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_DATE = "date";
    public static final String KEY_GENDER = "gender";

    // remmeber me variables
    private static final String IS_REMEMBERME = "IsRememberMe";
    public static final String KEY_SESSIONPHONENUMBER = "phoneNo";
    public static final String KEY_SESSIONPASSWORD = "password";

    // spotify
    private static final String IS_THEREISKEY = "isThereisKey";
    private static final String KEY_SPOTIFY = "spotifyKey";
    private static final String KEY_AUTH = "keyAuth";
    private static final String KEY_REFRESH = "keyRefresh";

    // Constructor
    public SessionManager(Context _context, String sessionName) {

        context = _context;
        usersSession = _context.getSharedPreferences(sessionName, Context.MODE_PRIVATE);
        editor = usersSession.edit();

    }

    public void createLoginSession(String fullname, String username, String email, String phoneNo, String password, String gender, String date) {

        editor = this.editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_FULLNAME, fullname);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PHONENUMBER, phoneNo);
        editor.putString(KEY_DATE, date);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_GENDER, gender);

        editor.commit();

    }

    public HashMap<String, String> getUsersDetailFromSession() {
        HashMap<String, String> userData = new HashMap<String, String>();
        userData.put(KEY_FULLNAME, usersSession.getString(KEY_FULLNAME, null));
        userData.put(KEY_USERNAME, usersSession.getString(KEY_USERNAME, null));
        userData.put(KEY_PHONENUMBER, usersSession.getString(KEY_PHONENUMBER, null));
        userData.put(KEY_DATE, usersSession.getString(KEY_DATE, null));
        userData.put(KEY_EMAIL, usersSession.getString(KEY_EMAIL, null));
        userData.put(KEY_GENDER, usersSession.getString(KEY_GENDER, null));
        userData.put(KEY_PASSWORD, usersSession.getString(KEY_PASSWORD, null));

        return userData;

    }

    public boolean checkLogin() {
        if (usersSession.getBoolean(IS_LOGIN, false)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkSpotify(){
        if(usersSession.getBoolean(IS_THEREISKEY, false)){
            return true;
        }else{
            return false;
        }
    }

    public void logoutUserSession() {
        editor.clear();
        editor.commit();
    }

    public void createRememberMeSession(String phoneNo, String password) {
        editor = this.editor.putBoolean(IS_REMEMBERME, true);
        editor.putString(KEY_SESSIONPHONENUMBER, phoneNo);
        editor.putString(KEY_SESSIONPASSWORD, password);
        editor.commit();
    }

    public void createSpotifySession(String authorizationKey, String auth){

        editor = this.editor.putBoolean(IS_THEREISKEY, true);
        editor.putString(KEY_SPOTIFY, authorizationKey);
        editor.putString(KEY_AUTH, auth);
        editor.commit();
    }

    public void generateNewKey(String authorizationKey){
        editor.putString(KEY_SPOTIFY, authorizationKey);
    }

    public String getKeySpotify(){
        return usersSession.getString(KEY_SPOTIFY, null);
    }

    public HashMap<String, String> getRememberMeDetailFromSession() {
        HashMap<String, String> userData = new HashMap<String, String>();

        userData.put(KEY_SESSIONPHONENUMBER, usersSession.getString(KEY_SESSIONPHONENUMBER, null));
        userData.put(KEY_SESSIONPASSWORD, usersSession.getString(KEY_SESSIONPASSWORD, null));

        return userData;

    }

    public boolean checkRememberMe() {
        if (usersSession.getBoolean(IS_REMEMBERME, false)) {
            return true;
        } else {
            return false;
        }
    }


}
