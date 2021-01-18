package com.glitchstacks.musiczone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.glitchstacks.musiczone.Common.ExploreDashboard;
import com.glitchstacks.musiczone.Database.SessionManager;
import com.glitchstacks.musiczone.Entries.SignUp2ndClass;
import com.glitchstacks.musiczone.Entries.SignUpActivity3rdClass;
import com.glitchstacks.musiczone.LocationOwner.ExploreDashboardFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddPlaylist extends AppCompatActivity {

    private EditText artistNameInput, artistLinkInput, songNameInput, songLinkInput;
    private DatabaseReference mDatabase;
    private String artistName, artistLink, songName, songLink;
    private TextInputLayout artistNameLayout, artistLinkLayout, songNameLayout, songLinkLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_playlist);

        SessionManager sessionManager = new SessionManager(AddPlaylist.this, SessionManager.SESSION_USERSESSION);

        // User Information map
        HashMap<String, String> map = sessionManager.getUsersDetailFromSession();

        // UserID
        String phoneNumber = map.get(SessionManager.KEY_PHONENUMBER);

        //Hooks
        Button nextBtn = (Button)findViewById(R.id.btnNext);
        Button add_song = (Button)findViewById(R.id.btnAddSong);

        artistNameLayout = findViewById(R.id.artist_layout);
        artistLinkLayout = findViewById(R.id.artist_link_layout);
        songNameLayout = findViewById(R.id.song_layout);
        songLinkLayout = findViewById(R.id.song_link_layout);

        artistNameInput = findViewById(R.id.artist_input);
        artistLinkInput = findViewById(R.id.artist_link_input);
        songNameInput = findViewById(R.id.song_input);
        songLinkInput = findViewById(R.id.song_link_input);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        add_song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               saveSongInformation();
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callNextScreen();
            }
        });

    }

    private Boolean isArtistNameValid() {

        artistName = artistNameInput.getText().toString();
        if(artistName.isEmpty()){
            artistNameLayout.setError("field can't be empty");
            return false;
        } else {
            artistNameLayout.setError(null);
            artistNameLayout.setErrorEnabled(false);
        }

        return true;
    }

    private Boolean isArtistLinkValid() {

        artistLink = artistLinkInput.getText().toString();
        if(artistLink.isEmpty()){
            artistLinkLayout.setError("field can't be empty");
            return false;
        } else {
            artistLinkLayout.setError(null);
            artistLinkLayout.setErrorEnabled(false);
        }

        return true;
    }

    private Boolean isSongNameValid() {

        songName = songNameInput.getText().toString();
        if(songName.isEmpty()){
            songNameLayout.setError("field can't be empty");
            return false;
        } else {
            songNameLayout.setError(null);
            songNameLayout.setErrorEnabled(false);
        }

        return true;
    }

    private Boolean isSongLinkValid() {

        songLink = songLinkInput.getText().toString();
        if(songLink.isEmpty()){
            songLinkLayout.setError("field can't be empty");
            return false;
        } else {
            songLinkLayout.setError(null);
            songLinkLayout.setErrorEnabled(false);
        }

        return true;
    }

    private void saveSongInformation() {

        Boolean a,b,c,d;
        a = isArtistNameValid();
        b = isArtistLinkValid();
        c = isSongNameValid();
        d = isSongLinkValid();

        if(!a || !b || !c || !d){
            return;
        }

        String playlistName = getIntent().getStringExtra("playlistName");
        String key = getIntent().getStringExtra("key");

        artistName = artistNameInput.getText().toString();
        artistLink = artistLinkInput.getText().toString();
        songName = songNameInput.getText().toString();
        songLink = songLinkInput.getText().toString();

        Log.d("cek Key", key);
        Log.d("cek PlaylistName", playlistName);

        DatabaseReference artistDatabase = mDatabase.child("Playlists").child(key).child(artistName);
        artistDatabase.child("artist_link").setValue(artistLink);

        final Map songInfo = new HashMap();
        songInfo.put("song_name", songName);
        songInfo.put("song_link", songLink);

        // Database Reference
        final String key2 = artistDatabase.child("songs").push().getKey();
        artistDatabase.child("songs").child(key2).updateChildren(songInfo);
        Toast.makeText(this, "Artist and song is now saved!", Toast.LENGTH_SHORT).show();

    }

    public void callNextScreen() {

        String playlistName = getIntent().getStringExtra("playlistName");
        String key = getIntent().getStringExtra("key");

        Intent intent = new Intent(getApplicationContext(), AddArea.class);
        intent.putExtra("key",key);
        intent.putExtra("playlistName",playlistName);

        startActivity(intent);

    }



}