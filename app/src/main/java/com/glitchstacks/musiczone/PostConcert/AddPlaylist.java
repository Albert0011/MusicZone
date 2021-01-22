package com.glitchstacks.musiczone.PostConcert;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.glitchstacks.musiczone.Database.SessionManager;
import com.glitchstacks.musiczone.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddPlaylist extends AppCompatActivity {

    private EditText artistNameInput, artistLinkInput, songNameInput, songLinkInput;
    private DatabaseReference mDatabase;
    private String artistName, artistLink, songName, songLink;
    private TextInputLayout artistNameLayout, artistLinkLayout, songNameLayout, songLinkLayout;
    private String concertName, concertKey, concertMainGenre, concertDescription, concertDuration, concertDate, concertTime;
    private Uri concertImageUrl;
    private ArrayList<Song> playlist;
    private Dialog dialog;


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

        playlist = new ArrayList<Song>();

        // Hook from previous Intent

        concertName = getIntent().getStringExtra("concertName");
        concertKey = getIntent().getStringExtra("concertKey");
        concertMainGenre = getIntent().getStringExtra("concertMainGenre");
        concertDescription = getIntent().getStringExtra("concertDescription");
        concertDuration = getIntent().getStringExtra("concertDuration");
        concertDate = getIntent().getStringExtra("concertDate");
        concertTime = getIntent().getStringExtra("concertTime");
        concertImageUrl = getIntent().getParcelableExtra("concertimageUri");

        Log.d("IniAddPlaylist", "something is missing" + concertName + concertKey + concertMainGenre + concertDescription + concertDuration + concertDate + concertTime + (concertImageUrl == null));

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

        String key = getIntent().getStringExtra("key");

        artistName = artistNameInput.getText().toString();
        artistLink = artistLinkInput.getText().toString();
        songName = songNameInput.getText().toString();
        songLink = songLinkInput.getText().toString();

        Song currentSong = new Song(songName, songLink, artistName, artistLink);
        for (Song cs:playlist) {
            if(cs.getSong_name().equals(songName) && cs.getArtist_name().equals(artistName)){
                Toast.makeText(this, "There is a duplicate song and artist!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Toast.makeText(this, "The playlist is successfully added!", Toast.LENGTH_SHORT).show();
        playlist.add(currentSong);

    }

    public void callNextScreen() {

        Intent intent = new Intent(getApplicationContext(), AddArea.class);

        if(concertName.isEmpty() || concertKey.isEmpty() || concertMainGenre.isEmpty() || concertDate.isEmpty() || concertDuration.isEmpty() || concertImageUrl == null || playlist.size() == 0 || concertTime.isEmpty() ){
            Log.d("AddArea", "something is missing" + concertName + concertKey + concertMainGenre + concertDescription + concertDuration + concertDate + concertTime + (concertImageUrl == null));
            return;
        }

        intent.putExtra("concertName", concertName);
        intent.putExtra("concertKey", concertKey);
        intent.putExtra("concertMainGenre", concertMainGenre);
        intent.putExtra("concertDescription", concertDescription);
        intent.putExtra("concertDuration", concertDuration);
        intent.putExtra("concertDate", concertDate);
        intent.putExtra("concertTime", concertTime);
        intent.putExtra("concertimageUri", concertImageUrl);
        intent.putExtra("playlist", playlist);
        startActivity(intent);

    }



}