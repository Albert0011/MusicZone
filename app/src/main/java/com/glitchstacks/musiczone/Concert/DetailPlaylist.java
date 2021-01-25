package com.glitchstacks.musiczone.Concert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.glitchstacks.musiczone.R;
import com.glitchstacks.musiczone.Track;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailPlaylist extends AppCompatActivity {

    private TextView txtArtistName;
    private ImageView imgArtist;
    private RecyclerView rvPlaylist;
    String concertKey, artistID;
    private String artistName, artistLink, artistImageUrl;
    private ArrayList<Track> trackArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_playlist);

        txtArtistName = findViewById(R.id.artist_name);
        imgArtist = findViewById(R.id.artist_image);
        rvPlaylist = findViewById(R.id.recyclerViewTrack);

        if(rvPlaylist == null){
            Log.d("rvPlaylist", "null");
        }

        concertKey = getIntent().getStringExtra("concertKey");
        artistID = getIntent().getStringExtra("artistID");


        Log.d("masukPlaylistDetail", concertKey+artistID);

        loadArtistInformation();
        loadPlaylistInformation();
    }

    private void loadArtistInformation() {

        DatabaseReference mArtist = FirebaseDatabase.getInstance().getReference().child("Playlists").child(concertKey).child(artistID);

        mArtist.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();

                    artistImageUrl = map.get("artist_image_url").toString();
                    artistLink = map.get("artist_spotify_link").toString();
                    artistName = map.get("artist_name").toString();

                    txtArtistName.setText(artistName);

                    Log.d("imageURL", artistImageUrl);
//
                    if(artistImageUrl!=null){
                        switch (artistImageUrl) {
                            case "default":
                                Glide.with(DetailPlaylist.this.getApplication()).load(R.mipmap.ic_launcher).into(imgArtist);
                                break;
                            default:
                                Glide.clear(imgArtist);
                                Glide.with(DetailPlaylist.this.getApplication()).load(artistImageUrl).into(imgArtist);
                                break;
                        }
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadPlaylistInformation() {

        DatabaseReference mPlaylist = FirebaseDatabase.getInstance().getReference().child("Playlists").child(concertKey).child(artistID).child("tracklist");

        mPlaylist.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    Iterable<DataSnapshot> currentSnap = snapshot.getChildren();

                    for(DataSnapshot s : currentSnap){
                        Map<String, Object> map = (Map<String, Object>) s.getValue();

                        String track_title = map.get("music_title").toString();
                        String url = map.get("music_url").toString();

                        Track currentTrack = new Track(url, track_title, s.getKey());
                        trackArrayList.add(currentTrack);
                    }

                    ConcertDetailTrackAdapter concertDetailTrackAdapter = new ConcertDetailTrackAdapter(trackArrayList);
                    rvPlaylist.setHasFixedSize(true);
                    rvPlaylist.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
                    rvPlaylist.setAdapter(concertDetailTrackAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}