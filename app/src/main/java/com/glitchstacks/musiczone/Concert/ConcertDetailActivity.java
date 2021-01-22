package com.glitchstacks.musiczone.Concert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.glitchstacks.musiczone.Matches.SwipeActivity;
import com.glitchstacks.musiczone.Database.SessionManager;
import com.glitchstacks.musiczone.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ConcertDetailActivity extends AppCompatActivity {

    private ImageView concertImage;
    private TextView concertNameText, concertTimeText, concertDateText, concertDurationText, concertDetailText;
    private DatabaseReference mDatabase;
    private String concertName, concertTime, concertDate, concertDuration, concertDetail, concertKey, imageURL;
    private FloatingActionButton btnLove;
    private LinearLayout findFriendsLayout, buyTicketLayout;
    private Integer loveButtonClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_concert_detail);

        // Hook

        Log.d("ConcertDetailMasuk", "ConcertDetailMasuk");

        loveButtonClicked = 0;

        // for Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // for TextView
        concertNameText = findViewById(R.id.txtConcertName);
        concertDateText = findViewById(R.id.txtDate);
        concertDurationText = findViewById(R.id.txtDuration);
        concertTimeText = findViewById(R.id.txtTime);
        concertDetailText = findViewById(R.id.txtConcertDetail);

        // for Image
        concertImage = findViewById(R.id.imgConcertImage);

        // for Button
        btnLove = findViewById(R.id.btnLove);

        // for Layout
        findFriendsLayout = findViewById(R.id.layoutFriends);
        buyTicketLayout = findViewById(R.id.layoutTicket);

        // Getting from the Intent
        concertKey = getIntent().getStringExtra("concertKey");

        // set Button Listener
        findFriendsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMatchesScreen();
            }
        });

        buyTicketLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToTicketScreen();
            }
        });

        btnLove.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("loveButtonClicked", "true");
                addUserToConcertLike();
            }
        });

        SessionManager sessionManager = new SessionManager(ConcertDetailActivity.this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> userDetails = sessionManager.getUsersDetailFromSession();

        final String phoneNumber = userDetails.get(SessionManager.KEY_PHONENUMBER);

        final DatabaseReference mUserLike = mDatabase.child("Concerts").child(concertKey).child("likedBy");

        mUserLike.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, Object> map = (Map<String, Object>)dataSnapshot.getValue();
                    // Hook
                    if(dataSnapshot.child(phoneNumber).exists()){
                        btnLove.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.heart128));
                    }else{
                        btnLove.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.circle_love));
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        Log.d("intentActivityConcertDetail", concertKey);

        // Mengisi TextView

        // Mengisi Informasi
        loadConcertInformation();

    }

    private void addUserToConcertLike() {

        Log.d("masukKeDalamAddConcertLikte", "True");

        SessionManager sessionManager = new SessionManager(ConcertDetailActivity.this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> userDetails = sessionManager.getUsersDetailFromSession();

        final String phoneNumber = userDetails.get(SessionManager.KEY_PHONENUMBER);

        final DatabaseReference mUserLike = mDatabase.child("Concerts").child(concertKey).child("likedBy");
        Log.d("masukStelahDatabaseReference", "True");
        Log.d("phoneNumber", phoneNumber);
        Log.d("concertKey", concertKey);

        if(mUserLike != null){
            Log.d("mUsertidakNull", "True");
        }

        mUserLike.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Log.d("masukSnapshot", "True");

                    Map<String, Object> map = (Map<String, Object>)dataSnapshot.getValue();
                    // Hook
                    if(dataSnapshot.child(phoneNumber).exists()){
                        Log.d("masukExist", "True");
                        mUserLike.child(phoneNumber).removeValue();
                        btnLove.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.circle_love));
                        mDatabase.child("Users").child(phoneNumber).child("concertLikes").child(concertKey).removeValue();
                    }else{
                        mDatabase.child("Concerts").child(concertKey).child("likedBy").child(phoneNumber).setValue("true");
                        btnLove.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.heart128));
                        mDatabase.child("Users").child(phoneNumber).child("concertLikes").child(concertKey).setValue("true");
                    }
                } else if(!dataSnapshot.exists()){
                    mDatabase.child("Concerts").child(concertKey).child("likedBy").child(phoneNumber).setValue("true");
                    btnLove.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.heart128));
                    mDatabase.child("Users").child(phoneNumber).child("concertLikes").child(concertKey).setValue("true");
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void goToTicketScreen() {

    }

    private void goToMatchesScreen() {
        Intent intent = new Intent(ConcertDetailActivity.this, SwipeActivity.class);
        intent.putExtra("concertKey", concertKey);
        startActivity(intent);
    }

    private void loadConcertInformation() {

        DatabaseReference mConcert = mDatabase.child("Concerts").child(concertKey);

        mConcert.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    // Hook
                    concertName = map.get("concert_name").toString();
                    concertDate = map.get("date").toString();
                    concertDetail = map.get("description").toString();
                    concertDuration = map.get("duration").toString();
                    concertTime = map.get("time").toString();

                    // Mengisi Object
                    concertNameText.setText(concertName);
                    concertDateText.setText(concertDate);
                    concertDetailText.setText(concertDetail);
                    concertTimeText.setText(concertTime);
                    concertDurationText.setText(concertDuration);

                    if (map.get("imageURL") != null) {
                        imageURL = map.get("imageURL").toString();
                        switch (imageURL) {
                            case "default":
                                Glide.with(getApplication()).load(R.mipmap.ic_launcher).into(concertImage);
                                break;
                            default:
                                Glide.clear(concertImage);
                                Glide.with(getApplication()).load(imageURL).into(concertImage);

                                break;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}