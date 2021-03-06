package com.glitchstacks.musiczone.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import com.glitchstacks.musiczone.Database.SessionManager;
import com.glitchstacks.musiczone.R;
import com.glitchstacks.musiczone.Search.SearchPageRecycler.ConcertAdapter;
import com.glitchstacks.musiczone.Search.SearchPageRecycler.ConcertObject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class SavedConcerts extends AppCompatActivity {

    private Integer currentChoosen;
    private SearchView searchView;
    private ImageView backButton;
    private RecyclerView concertRecyclerByName, concertRecyclerByGenre, concertRecyclerByArtist, concertRecyclerByCity;
    private ArrayList<ConcertObject> concertListByName;
    private DatabaseReference mDatabase;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_concerts);

        // Hook

        searchView = findViewById(R.id.searchView);

        concertRecyclerByName = findViewById(R.id.recyclerViewName);
        concertRecyclerByGenre = findViewById(R.id.recyclerViewGenre);
        concertRecyclerByArtist = findViewById(R.id.recyclerViewArtist);
        concertRecyclerByCity = findViewById(R.id.recyclerViewCity);
        backButton = findViewById(R.id.back_btn);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();

        SessionManager sessionManager = new SessionManager(SavedConcerts.this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> userDetails = sessionManager.getUsersDetailFromSession();

        phoneNumber = userDetails.get(SessionManager.KEY_PHONENUMBER);
        concertListByName  = new ArrayList<>();

    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("startMasuk", "startMasuk");

        if(mDatabase != null){

            mDatabase.child("Users").child(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){

                        ArrayList<String> concertKeys = new ArrayList<>();

                        DataSnapshot concertLikeSnapshot = snapshot.child("concertLikes");

                        for(DataSnapshot s : concertLikeSnapshot.getChildren()){
                            concertKeys.add(s.getKey().toString());
                            Log.d("concertKeySvaed", s.getKey().toString());
                        }

                        getConcertInformation(concertKeys);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        if(searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    searchByName(newText);
                    searchByGenre(newText);
                    return true;
                }
            });

        }
    }

    private void getConcertInformation(ArrayList<String> concertKeys) {

        for(String s : concertKeys){

            Log.d("getConcertInformation", s);

            DatabaseReference mConcerts = FirebaseDatabase.getInstance().getReference().child("Concerts").child(s);

            if(mConcerts != null){
                Log.d("getConcertInformation", "there is a reference");
            }

            concertListByName.clear();

            mConcerts.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {



                    if(snapshot.exists()){

                        String imageUrl = null, concertTitle = null, concertDesc = null, concertDate = null, concertTime = null, concertKey=null, concertMainGenre;
                        // Hook from database
                        imageUrl = snapshot.child("imageURL").getValue().toString();
                        concertTitle = snapshot.child("concert_name").getValue().toString();
                        concertDesc = snapshot.child("description").getValue().toString();
                        concertDate = snapshot.child("date").getValue().toString();
                        concertTime = snapshot.child("time").getValue().toString();
                        concertKey = snapshot.child("id").getValue().toString();
                        concertMainGenre = snapshot.child("main_genre").getValue().toString();

                        if(!imageUrl.isEmpty() && !concertTitle.isEmpty() && !concertDesc.isEmpty() && !concertDate.isEmpty()
                               ){

                            String messages = imageUrl + " " + concertTitle + " " + concertDesc + " " + concertDate;


                            ConcertObject concertObject = new ConcertObject(imageUrl, concertTitle, concertDesc, concertDate, concertKey, concertMainGenre);

                            concertListByName.add(concertObject);

                        }
                    }

                    ConcertAdapter concertAdapter = new ConcertAdapter(concertListByName);

                    Log.d("concertAdapter", String.valueOf(concertAdapter.getItemCount()));

                    concertRecyclerByName.setHasFixedSize(true);
                    concertRecyclerByName.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL));
                    concertRecyclerByName.setAdapter(concertAdapter);

                    concertRecyclerByGenre.setHasFixedSize(true);
                    concertRecyclerByGenre.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL));
                    concertRecyclerByGenre.setAdapter(concertAdapter);

                    }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
                });
        }
    }

    private void searchByName(String str) {

        Log.d("masukSearchConcert","masukSearchConcert");

        ArrayList<ConcertObject> myList = new ArrayList<>();


        for(ConcertObject object : concertListByName){
            if(object.getTitle().toLowerCase().contains(str.toLowerCase())){
                myList.add(object);
            }
        }

        ConcertAdapter concertAdapter = new ConcertAdapter(myList);
        concertRecyclerByName.setHasFixedSize(true);
        concertRecyclerByName.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL));
        concertRecyclerByName.setAdapter(concertAdapter);
    }

    private void searchByGenre(String str) {

        Log.d("masukSearchGenre","masukSearchGenre");

        ArrayList<ConcertObject> myList = new ArrayList<>();

        for(ConcertObject object : concertListByName){
            if(object.getGenre().toLowerCase().contains(str.toLowerCase())){
                myList.add(object);
            }
        }

        ConcertAdapter concertAdapter = new ConcertAdapter(myList);
        concertRecyclerByGenre.setHasFixedSize(true);
        concertRecyclerByGenre.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL));
        concertRecyclerByGenre.setAdapter(concertAdapter);
    }

    private void searchByCity(final String newText) {

        final ArrayList<ConcertObject> myList = new ArrayList<>();

        DatabaseReference mAddress = FirebaseDatabase.getInstance().getReference().child("Address");

        for(final ConcertObject object : concertListByName){

            mAddress.child(object.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.child("city").getValue().toString().toLowerCase().contains(newText.toLowerCase())){
                        myList.add(object);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        ConcertAdapter concertAdapter = new ConcertAdapter(myList);
        concertRecyclerByCity.setHasFixedSize(true);
        concertRecyclerByCity.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL));
        concertRecyclerByCity.setAdapter(concertAdapter);

    }

    private void searchByArtist(final String newText) {

        final ArrayList<ConcertObject> myList = new ArrayList<>();

        DatabaseReference mPlaylist = FirebaseDatabase.getInstance().getReference().child("Playlists");

        for(final ConcertObject object : concertListByName){

            mPlaylist.child(object.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for(DataSnapshot s : snapshot.getChildren()){
                        if(s.child("artist_name").getValue().toString().toLowerCase().contains(newText.toLowerCase())){
                            myList.add(object);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        ConcertAdapter concertAdapter = new ConcertAdapter(myList);
        concertRecyclerByArtist.setHasFixedSize(true);
        concertRecyclerByArtist.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL));
        concertRecyclerByArtist.setAdapter(concertAdapter);

    }




}