package com.glitchstacks.musiczone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.glitchstacks.musiczone.HelperClasses.ExplorePageAdapter.MostViewedAdapter;
import com.glitchstacks.musiczone.HelperClasses.ExplorePageAdapter.MostViewedHelperClass;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MostViewedPage extends AppCompatActivity {

    private ImageView back_btn;
    private RecyclerView mostViewedRecycler;
    private RecyclerView.Adapter mostViewedAdapter;
    private ArrayList<MostViewedHelperClass> mostViewedList = new ArrayList<MostViewedHelperClass>();
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_most_viewed_page);
        mostViewedRecycler = findViewById(R.id.most_viewed_recycler);
        back_btn = findViewById(R.id.back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mostViewedRecycler();
        mostViewedAdapter = new MostViewedAdapter(getPopular());
        mostViewedRecycler.setAdapter(mostViewedAdapter);

    }


    private ArrayList<MostViewedHelperClass> getPopular() {
        return mostViewedList;
    }


    private void mostViewedRecycler() {

        mostViewedRecycler.setHasFixedSize(true);
        mostViewedRecycler.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));

        mDatabase.child("Concerts").orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){

                    Log.d("child detected", "child detected");

                    String imageUrl = null, concertTitle = null, concertDesc = null, concertDate = null, concertTime = null, concertKey=null, concertMainGenre;
                    Integer viewer;

                    // retreive currentDate
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String currentdate = dateFormat.format(calendar.getTime());


                    // Hook from database
                    imageUrl = snapshot.child("imageURL").getValue().toString();
                    concertTitle = snapshot.child("concert_name").getValue().toString();
                    concertDesc = snapshot.child("description").getValue().toString();
                    concertDate = snapshot.child("date").getValue().toString();
                    concertTime = snapshot.child("time").getValue().toString();
                    concertKey = snapshot.child("id").getValue().toString();
                    concertMainGenre = snapshot.child("main_genre").getValue().toString();

                    String temp_date = concertDate + " " + concertTime;

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

                    Date temp_date1 = null;

                    if(snapshot.child("concertView").exists()){
                        viewer = Integer.parseInt(String.valueOf(snapshot.child("concertView").getChildrenCount()));
                    }else{
                        viewer = 0;
                    }

                    try {
                        temp_date1 = simpleDateFormat.parse(temp_date);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    if(viewer >= 2 && !imageUrl.isEmpty() && !concertTitle.isEmpty() && !concertDesc.isEmpty() && !concertDate.isEmpty()
                            && temp_date1.after(calendar.getTime())){

                        String messages = imageUrl + " " + concertTitle + " " + concertDesc + " " + concertDate;

                        Log.d("child detected", messages );

                        MostViewedHelperClass mostViewedHelperClass = new MostViewedHelperClass(imageUrl, concertTitle, concertDesc, concertDate, concertKey, concertMainGenre);

                        mostViewedList.add(mostViewedHelperClass);
                        mostViewedAdapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }

}