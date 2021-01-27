package com.glitchstacks.musiczone.Dashboard.DashboardFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.glitchstacks.musiczone.HelperClasses.ExplorePageAdapter.FeaturedAdapter;
import com.glitchstacks.musiczone.HelperClasses.ExplorePageAdapter.FeaturedHelperClass;
import com.glitchstacks.musiczone.HelperClasses.ExplorePageAdapter.MostViewedAdapter;
import com.glitchstacks.musiczone.HelperClasses.ExplorePageAdapter.MostViewedHelperClass;
import com.glitchstacks.musiczone.MostViewedPage;
import com.glitchstacks.musiczone.R;
import com.glitchstacks.musiczone.Search.SearchActivity;
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

public class ExploreDashboardFragment extends Fragment {

    private RecyclerView featuredRecycler;
    private RecyclerView.Adapter featuredAdapter;

    private RecyclerView mostViewedRecycler;
    private RecyclerView.Adapter mostViewedAdapter;

    private DatabaseReference mDatabase;

    private TextView searchLayout, view_all;

    private ArrayList<FeaturedHelperClass> featuredList = new ArrayList<FeaturedHelperClass>();
    private ArrayList<MostViewedHelperClass> mostViewedList = new ArrayList<MostViewedHelperClass>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_explore_dashboard, container, false);

        searchLayout = root.findViewById(R.id.search_input);
        view_all = root.findViewById(R.id.view_all);

        view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MostViewedPage.class);
                startActivity(intent);
            }
        });

        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        // Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // list of featured concerts
        featuredRecycler = root.findViewById(R.id.featured_recycler);
        featuredRecycler();

        // list of mostViewedConcerts
        mostViewedRecycler = root.findViewById(R.id.most_viewed_recycler);
        mostViewedRecycler();

        featuredAdapter = new FeaturedAdapter(getFeatured());
        featuredRecycler.setAdapter(featuredAdapter);

        mostViewedAdapter = new MostViewedAdapter(getPopular());
        mostViewedRecycler.setAdapter(mostViewedAdapter);
        return root;
    }

    private ArrayList<MostViewedHelperClass> getPopular() {
        return mostViewedList;
    }

    private void featuredRecycler() {

        featuredRecycler.setHasFixedSize(true);
        featuredRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        mDatabase.child("Concerts").orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){

                    Log.d("child detected", "child detected");

                    String imageUrl = null, concertTitle = null, concertDesc = null, concertDate = null, concertKey = null, concerMainGenre;
                    Integer viewer;

                    // Retreive currentDate
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String currentdate = dateFormat.format(calendar.getTime());

                    // Hook from database
                    imageUrl = snapshot.child("imageURL").getValue().toString();
                    concertTitle = snapshot.child("concert_name").getValue().toString();
                    concertDesc = snapshot.child("description").getValue().toString();
                    concertDate = snapshot.child("date").getValue().toString();
                    concertKey = snapshot.child("id").getValue().toString();
                    concerMainGenre = snapshot.child("main_genre").getValue().toString();

                    // To be push
                    Log.d("dateequal1", currentdate);

                    if(!imageUrl.isEmpty() && !concertTitle.isEmpty() && !concertDesc.isEmpty() && !concertDate.isEmpty()
                            && concertDate.equals(currentdate)){

                        String messages = imageUrl + " " + concertTitle + " " + concertDesc + " " + concertDate;

                        Log.d("child detected", messages );

                        FeaturedHelperClass featuredHelperClass = new FeaturedHelperClass(imageUrl, concertTitle, concertDesc, concertDate, concertKey, concerMainGenre);

                        featuredList.add(featuredHelperClass);
                        featuredAdapter.notifyDataSetChanged();
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

    private void mostViewedRecycler() {

        mostViewedRecycler.setHasFixedSize(true);
        mostViewedRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

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

    private ArrayList<FeaturedHelperClass> getFeatured(){
        return featuredList;
    }



}