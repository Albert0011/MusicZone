package com.glitchstacks.musiczone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class ConcertDetailActivity extends AppCompatActivity {

    private ImageView concertImage;
    private TextView concertNameText, concertTimeText, concertDateText, concertDurationText, concertDetailText;
    private DatabaseReference mDatabase;
    private String concertName, concertTime, concertDate, concertDuration, concertDetail, concertKey, imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_concert_detail);

        // Hook

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

        // Getting from the Intent
        concertKey = getIntent().getStringExtra("concertKey");

        Log.d("intentActivityConcertDetail", concertKey);

        // Mengisi TextView

        // Mengisi Informasi
        loadConcertInformation();

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