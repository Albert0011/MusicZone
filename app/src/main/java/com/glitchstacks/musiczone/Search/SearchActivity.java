package com.glitchstacks.musiczone.Search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

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

public class SearchActivity extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView concertRecyclerByName;
    private ArrayList<ConcertObject> concertListByName;
    private RecyclerView concertRecyclerByGenre;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Hook

        searchView = findViewById(R.id.searchView);

        concertListByName = new ArrayList<ConcertObject>();

        concertRecyclerByName = findViewById(R.id.recyclerViewName);
        concertRecyclerByGenre = findViewById(R.id.recyclerViewGenre);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Concerts");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mDatabase != null){

            concertListByName.clear();

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){

                        for (DataSnapshot ds : snapshot.getChildren()){

                            String imageUrl = null, concertTitle = null, concertDesc = null, concertDate = null, concertTime = null, concertKey=null, concertMainGenre;
                            Integer viewer;

                            // retreive currentDate
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            String currentdate = dateFormat.format(calendar.getTime());

                            // Hook from database
                            imageUrl = ds.child("imageURL").getValue().toString();
                            concertTitle = ds.child("concert_name").getValue().toString();
                            concertDesc = ds.child("description").getValue().toString();
                            concertDate = ds.child("date").getValue().toString();
                            concertTime = ds.child("time").getValue().toString();
                            concertKey = ds.child("id").getValue().toString();
                            concertMainGenre = ds.child("main_genre").getValue().toString();

                            String temp_date = concertDate + " " + concertTime;

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

                            Date temp_date1 = null;

                            try {
                                temp_date1 = simpleDateFormat.parse(temp_date);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                            if(!imageUrl.isEmpty() && !concertTitle.isEmpty() && !concertDesc.isEmpty() && !concertDate.isEmpty()
                                    && temp_date1.after(calendar.getTime())){

                                String messages = imageUrl + " " + concertTitle + " " + concertDesc + " " + concertDate;

                                Log.d("child detected", messages );

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



}