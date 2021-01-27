package com.glitchstacks.musiczone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.glitchstacks.musiczone.Database.SessionManager;
import com.glitchstacks.musiczone.PostConcert.AddAddress;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class PromotorPage extends AppCompatActivity implements PromotorConcertListener{

    private TextView balance_textview;
    private RecyclerView concert_recyclerView;
    private Button remove_btn;
    private String phoneNumber;
    private RecyclerView.LayoutManager layoutManager;
    private ConcertAdapter concertAdapter;
    private ArrayList<PromotorConcert> concertList = new ArrayList<>();
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotor_page);

        balance_textview = findViewById(R.id.balance_textview);
        concert_recyclerView = findViewById(R.id.recylcerViewConcert);
        remove_btn = findViewById(R.id.remove_btn);

        concert_recyclerView.setHasFixedSize(true);
        concert_recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL));
        concertAdapter = new ConcertAdapter(getDataSet(), this);
        concert_recyclerView.setAdapter(concertAdapter);

        remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeSelectedConcert();
            }
        });

    }

    private ArrayList<PromotorConcert> getDataSet() {
        retrieveAllConcertInformation();
        return concertList;

    }

    private void retrieveAllConcertInformation() {

        SessionManager sessionManager = new SessionManager(PromotorPage.this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> map = sessionManager.getUsersDetailFromSession();
        String phoneNumber = map.get(SessionManager.KEY_PHONENUMBER);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Promotors").child(phoneNumber).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    Log.d("snapshot exist", "true");

                    for(DataSnapshot s : snapshot.getChildren()){

                        Log.d("children exist", s.getKey());
                        getConcertInformation(s.getKey());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getConcertInformation(final String key) {

        DatabaseReference mConcert = FirebaseDatabase.getInstance().getReference().child("Concerts").child(key);

        mConcert.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    Log.d("concertKeyExist", "true");

                    String concertKey = key;
                    String concertName = snapshot.child("concert_name").getValue().toString();
                    String concertDescription = snapshot.child("description").getValue().toString();
                    String concertDate = snapshot.child("date").getValue().toString();
                    String concertImageUrl = snapshot.child("imageURL").getValue().toString();

                    PromotorConcert currentConcert = new PromotorConcert(concertName, concertDescription, concertDate, concertImageUrl, concertKey);
                    concertList.add(currentConcert);
                    concertAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void removeSelectedConcert() {

        ArrayList<PromotorConcert> selectedConcert = concertAdapter.getSelectedConcert();
        for(final PromotorConcert c : selectedConcert){
            final DatabaseReference mConcert = FirebaseDatabase.getInstance().getReference().child("Concerts");
            final DatabaseReference mArea = FirebaseDatabase.getInstance().getReference().child("Area");
            final DatabaseReference mAddress = FirebaseDatabase.getInstance().getReference().child("Address");
            final DatabaseReference mPlaylists = FirebaseDatabase.getInstance().getReference().child("Playlists");

            final String concertKey = c.getConcertKey();

            mConcert.child(concertKey).removeValue();
            mArea.child(concertKey).removeValue();
            mAddress.child(concertKey).removeValue();
            mPlaylists.child(concertKey).removeValue();

            concertAdapter.notifyDataSetChanged();

            final DatabaseReference mUser = FirebaseDatabase.getInstance().getReference().child("Users");

            mUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for(DataSnapshot s : snapshot.getChildren()){
                            if((s.child("concertView").child(concertKey)).exists()){
                                mUser.child("concertView").child(concertKey).removeValue();
                            }
                            if((s.child("concertLikes").child(concertKey)).exists()){
                                mUser.child("concertLikes").child(concertKey).removeValue();
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void onConcertAction(Boolean isSelected) {
        if(isSelected){
            remove_btn.setVisibility(View.VISIBLE);
        }else{
            remove_btn.setVisibility(View.GONE);
        }

    }
}