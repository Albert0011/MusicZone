package com.glitchstacks.musiczone.LocationOwner;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.glitchstacks.musiczone.Database.SessionManager;
import com.glitchstacks.musiczone.Matches.MatchesActivity;
import com.glitchstacks.musiczone.Matches.MatchesAdapter;
import com.glitchstacks.musiczone.Matches.MatchesObject;
import com.glitchstacks.musiczone.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatDashboardFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mMatchesAdapter;
    private RecyclerView.LayoutManager mMatchesLayoutManager;

    private String currentUserID, phoneNumber;

    private ArrayList<MatchesObject> resultsMatches = new ArrayList<MatchesObject>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_chat_dashboard, container, false);

        SessionManager sessionManager = new SessionManager(getContext(), SessionManager.SESSION_USERSESSION);
        HashMap<String, String> map = sessionManager.getUsersDetailFromSession();
        phoneNumber = map.get(SessionManager.KEY_PHONENUMBER);

        currentUserID = phoneNumber;

        // Chat Recycle View
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);

        // Layout Manager for Chat Recycle View
        mMatchesLayoutManager = new LinearLayoutManager(getContext());

        // Set Chat RecycleView Layout to mMatchesLayoutManager
        mRecyclerView.setLayoutManager(mMatchesLayoutManager);

        // Setting Adapter for RecycleView
        mMatchesAdapter = new MatchesAdapter(getDataSetMatches(), getContext());
        mRecyclerView.setAdapter(mMatchesAdapter);

        return root;
    }

    private void getUserMatchId() {
        DatabaseReference matchDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("connections").child("matches");
        matchDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot match : dataSnapshot.getChildren()){
                        FetchMatchInformation(match.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void FetchMatchInformation(String key) {
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String userId = dataSnapshot.getKey();
                    String name = "";
                    String profileImageUrl = "";
                    if(dataSnapshot.child("fullname").getValue()!=null){
                        name = dataSnapshot.child("fullname").getValue().toString();
                    }
                    if(dataSnapshot.child("profileImageUrl").getValue()!=null){
                        profileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                    }


                    MatchesObject obj = new MatchesObject(userId, name, profileImageUrl);
                    resultsMatches.add(obj);
                    mMatchesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private List<MatchesObject> getDataSetMatches() {

        getUserMatchId();
        return resultsMatches;
    }





}