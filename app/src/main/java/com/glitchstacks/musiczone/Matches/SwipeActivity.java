package com.glitchstacks.musiczone.Matches;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.glitchstacks.musiczone.Cards.arrayAdapter;
import com.glitchstacks.musiczone.Cards.cards;
import com.glitchstacks.musiczone.Database.SessionManager;
import com.glitchstacks.musiczone.Profile.SettingsActivity;
import com.glitchstacks.musiczone.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SwipeActivity extends AppCompatActivity {
    private cards cards_data[];
    private com.glitchstacks.musiczone.Cards.arrayAdapter arrayAdapter;
    private int i;

    private String currentUId, concertKey;
    private String phoneNumber;
    private DatabaseReference usersDb, mDatabase;
    private ImageView back_btn;

    private ArrayList<String> userLikeList;


    ListView listView;
    List<cards> rowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");

        SessionManager sessionManager = new SessionManager(SwipeActivity.this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> map = sessionManager.getUsersDetailFromSession();
        phoneNumber = map.get(SessionManager.KEY_PHONENUMBER);
        currentUId = phoneNumber;
        concertKey = getIntent().getStringExtra("concertKey");
        userLikeList = new ArrayList<String>();

        back_btn = findViewById(R.id.back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        Toast.makeText(SwipeActivity.this, "MASUKSBLMSEX", Toast.LENGTH_SHORT).show();
        checkUserSex();
//        Toast.makeText(SwipeActivity.this, "AFTERSEX", Toast.LENGTH_SHORT).show();

        rowItems = new ArrayList<cards>();

        arrayAdapter = new arrayAdapter(this, R.layout.item,rowItems);

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
//                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {

                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userId).child("connections").child("nope").child(currentUId).setValue(true);
//                Toast.makeText(SwipeActivity.this, "Left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userId).child("connections").child("yeps").child(currentUId).setValue(true);

                isConnectionMatch(userId);
//                Toast.makeText(SwipeActivity.this, "Right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });

        getConcertMatches();

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
//                Toast.makeText(SwipeActivity.this, "Item Clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void isConnectionMatch(String userId) {
        DatabaseReference currentUserConnectionsDb = usersDb.child(currentUId).child("connections").child("yeps").child(userId);
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Toast.makeText(SwipeActivity.this, "New connection made!", Toast.LENGTH_LONG).show();

                    String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();

                    usersDb.child(dataSnapshot.getKey()).child("connections").child("matches").child(currentUId).child("ChatId").setValue(key);
                    usersDb.child(currentUId).child("connections").child("matches").child(dataSnapshot.getKey()).child("ChatId").setValue(key);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private String userSex;
    private String oppositeUserSex;
    public void checkUserSex(){
        //final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        SessionManager sessionManager = new SessionManager(SwipeActivity.this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> map = sessionManager.getUsersDetailFromSession();
        phoneNumber = map.get(SessionManager.KEY_PHONENUMBER);

        DatabaseReference userDb = usersDb.child(phoneNumber);
//        Toast.makeText(SwipeActivity.this, phoneNumber, Toast.LENGTH_SHORT).show();

        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("gender").getValue() != null){
                        userSex = dataSnapshot.child("gender").getValue().toString();
                        switch (userSex){
                            case "Male":
                                oppositeUserSex = "Female";
                                break;
                            case "Female":
                                oppositeUserSex = "Male";
                                break;
                        }
                        getOppositeSexUsers();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getConcertMatches(){
        mDatabase.child("Concerts").child(concertKey).child("likedBy").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    String userID = snapshot.getKey();
                    userLikeList.add(userID);
                    Log.d("UserIniMasuk", userID);
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

    public void getOppositeSexUsers(){
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String snapshotID = dataSnapshot.getKey();

                if (dataSnapshot.child("gender").getValue() != null) {
                    if (dataSnapshot.exists() && !dataSnapshot.child("connections").child("nope").hasChild(currentUId) && !dataSnapshot.child("connections").child("yeps").hasChild(currentUId)
                    && userLikeList.contains(snapshotID) && (!currentUId.equals(snapshotID))) {

                        Log.d("UserIniMasukKarena", "currentUserID = " + snapshotID + ", " + currentUId);

//                        Toast.makeText(SwipeActivity.this, dataSnapshot.child("fullname").getValue().toString(), Toast.LENGTH_SHORT).show();
                        String profileImageUrl = "default";
                        if (dataSnapshot.child("profileImageUrl").exists()) {
                            profileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                        } else {
                            profileImageUrl = "https://firebasestorage.googleapis.com/v0/b/musiczone-90ae7.appspot.com/o/profileImages%2Fblank-profile-picture-973460_960_720.png?alt=media&token=51b5959f-f91c-4148-8b0b-52603c42effd";
                        }

                        String date = dataSnapshot.child("date").getValue().toString();
                        Integer year = Calendar.getInstance().get(Calendar.YEAR);
                        date = date.substring(date.length() - 4);
                        Integer birthDate = Integer.parseInt(date);
                        Integer currentDate = year - birthDate;
                        String dateStr = currentDate.toString();

                        cards item = new cards(dataSnapshot.getKey(), dataSnapshot.child("fullname").getValue().toString(), profileImageUrl,
                                dataSnapshot.child("description").getValue().toString(), dateStr);
                        rowItems.add(item);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void goToSettings(View view) {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    public void goToMatches(View view) {
        Intent intent = new Intent(getApplicationContext(), MatchesActivity.class);
        startActivity(intent);
        return;
    }
}