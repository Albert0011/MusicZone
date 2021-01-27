package com.glitchstacks.musiczone.Concert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.glitchstacks.musiczone.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PlacesDetail extends AppCompatActivity {

    private TextView place, address, city, province, note;
    private ImageView back_btn, concert_image;
    String concertKey, imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_detail);

        place = findViewById(R.id.place);
        address = findViewById(R.id.address);
        city = findViewById(R.id.city);
        province = findViewById(R.id.province);
        note = findViewById(R.id.note);
        concert_image = findViewById(R.id.image);
        back_btn = findViewById(R.id.back_btn);

        concertKey = getIntent().getStringExtra("concertKey");

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDatabase.child("Concerts").child(concertKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    imageURL = snapshot.child("imageURL").getValue().toString();

                    switch (imageURL) {
                        case "default":
                            Glide.with(getApplication()).load(R.mipmap.ic_launcher).into(concert_image);
                            break;
                        default:
                            try {
                                Glide.clear(concert_image);
                                Glide.with(getApplication()).load(imageURL).into(concert_image);
                            }catch(Exception e){

                            }
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        mDatabase.child("Address").child(concertKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    String concert_address, concert_city, concert_province, concert_note, concert_place;

                    concert_address = snapshot.child("address").getValue().toString();
                    concert_city = snapshot.child("city").getValue().toString();
                    concert_province = snapshot.child("province").getValue().toString();
                    concert_note = snapshot.child("desc").getValue().toString();
                    concert_place = snapshot.child("place").getValue().toString();

                    place.setText(concert_place);
                    note.setText(concert_note);
                    city.setText(concert_city);
                    address.setText(concert_address);
                    province.setText(concert_province);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
}