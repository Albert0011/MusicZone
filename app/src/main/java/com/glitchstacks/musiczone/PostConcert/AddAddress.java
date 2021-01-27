package com.glitchstacks.musiczone.PostConcert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.glitchstacks.musiczone.Dashboard.RetailerDashboard;
import com.glitchstacks.musiczone.Database.SessionManager;
import com.glitchstacks.musiczone.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddAddress extends AppCompatActivity {


    private EditText provinceInput, cityInput, addressInput, placeInput, descInput;
    private DatabaseReference mDatabase;
    private TextInputLayout provinceLayout, cityLayout, addressLayout, placeLayout, descLayout;
    private String province, city, address, place, desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        SessionManager sessionManager = new SessionManager(AddAddress.this, SessionManager.SESSION_USERSESSION);

        // User Information map
        HashMap<String, String> map = sessionManager.getUsersDetailFromSession();

        // UserID
        String phoneNumber = map.get(SessionManager.KEY_PHONENUMBER);

        Button nextBtn = (Button)findViewById(R.id.btnNext);

        provinceLayout = findViewById(R.id.province_layout);
        cityLayout = findViewById(R.id.city_layout);
        addressLayout = findViewById(R.id.address_layout);
        placeLayout = findViewById(R.id.place_layout);
        descLayout = findViewById(R.id.desc_layout);

        provinceInput = findViewById(R.id.province_input);
        cityInput = findViewById(R.id.city_input);
        addressInput = findViewById(R.id.address_input);
        placeInput = findViewById(R.id.place_input);
        descInput = findViewById(R.id.desc_input);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callNextScreen();
            }
        });

    }

    private Boolean isProvinceValid() {
        province = provinceInput.getText().toString();
        if(province.isEmpty()){
            provinceLayout.setError("field can't be empty");
            return false;
        } else {
            provinceLayout.setError(null);
            provinceLayout.setErrorEnabled(false);
        }
        return true;
    }

    private Boolean isCityValid() {

        city = cityInput.getText().toString();
        if(city.isEmpty()){
            cityLayout.setError("field can't be empty");
            return false;
        } else {
            cityLayout.setError(null);
            cityLayout.setErrorEnabled(false);
        }
        return true;
    }

    private Boolean isPlaceValid() {

        place = placeInput.getText().toString();
        if(place.isEmpty()){
            placeLayout.setError("field can't be empty");
            return false;
        } else {
            placeLayout.setError(null);
            placeLayout.setErrorEnabled(false);
        }

        return true;
    }

    private Boolean isAddressValid() {

        address = addressInput.getText().toString();
        if(address.isEmpty()){
            addressLayout.setError("field can't be empty");
            return false;
        } else {
            addressLayout.setError(null);
            addressLayout.setErrorEnabled(false);
        }

        return true;
    }

    private Boolean isDescValid() {

        desc = descInput.getText().toString();
        if(desc.isEmpty()){
            descLayout.setError("field can't be empty");
            return false;
        } else {
            descLayout.setError(null);
            descLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean savePlaceInformation() {

        Boolean a,b,c,d,e;

        a = isAddressValid();
        b = isPlaceValid();
        c = isCityValid();
        d = isDescValid();
        e = isProvinceValid();

        if(!a || !b || !c || !d || !e){
            return false;
        }

        final String concertName = getIntent().getStringExtra("concertName");
        final String concertKey = getIntent().getStringExtra("concertKey");
        final String concertMainGenre = getIntent().getStringExtra("concertMainGenre");
        final String concertDescription = getIntent().getStringExtra("concertDescription");
        final String concertDuration = getIntent().getStringExtra("concertDuration");
        final String concertDate = getIntent().getStringExtra("concertDate");
        final String concertTime = getIntent().getStringExtra("concertTime");
        Uri concertImageUri = getIntent().getParcelableExtra("concertimageUri");
        Uri areaImageUri = getIntent().getParcelableExtra("areaUri");
        ArrayList<Performance> playlist = (ArrayList<Performance>) getIntent().getSerializableExtra("playlist");
        ArrayList<Area> areaList = (ArrayList<Area>) getIntent().getSerializableExtra("areaList");

        // save concertInformation

        String sementara = concertName + concertKey +  concertMainGenre + concertDescription +  concertDate + concertTime + concertDuration;

        Log.d("concert Added", sementara);

        if(!(concertImageUri==null)){
            Log.d("concertImageNotNull", "in");
            final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("concertImages").child(concertKey);

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(),concertImageUri);
            } catch (IOException s) {
                s.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = filepath.putBytes(data);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUrl) {
                            Log.d("concertImageUpdateSuccessfull","uploadSuccess");
                            Map userInfo = new HashMap();
                            userInfo.put("concert_name", concertName);
                            userInfo.put("id", concertKey);
                            userInfo.put("main_genre", concertMainGenre);
                            userInfo.put("description", concertDescription);
                            userInfo.put("duration", concertDuration);
                            userInfo.put("date", concertDate);
                            userInfo.put("time", concertTime);
                            userInfo.put("imageURL", downloadUrl.toString());

                            SessionManager sessionManager = new SessionManager(AddAddress.this, SessionManager.SESSION_USERSESSION);
                            HashMap<String, String> map = sessionManager.getUsersDetailFromSession();
                            String phoneNumber = map.get(SessionManager.KEY_PHONENUMBER);

                            userInfo.put("promotor", phoneNumber);
                            mDatabase.child("Concerts").child(concertKey).updateChildren(userInfo);
                            mDatabase.child("Promotors").child(phoneNumber).child(concertKey).setValue("true");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("uploadFail",e.getMessage().toString());
                        }
                    });
                }
            });
        } else {
            Log.d("resultUri", "out");
            return false;
        }

        // Save Concert Playlist

        DatabaseReference mPlaylist = mDatabase.child("Playlists").child(concertKey);

        for(Performance p : playlist){
            Map artistInfo = new HashMap();

            String id = p.getArtist().getId();
            artistInfo.put("artist_name",p.getArtist().getName());
            artistInfo.put("artist_image_url",p.getArtist().getImageURL());
            artistInfo.put("artist_spotify_link",p.getArtist().getSpotifyLink());

            mPlaylist.child(id).updateChildren(artistInfo);
        }

        for(Performance p : playlist){
            String id = p.getArtist().getId();
            for(Track currentTrackList : p.getTrackList()){
                Map trackInfo = new HashMap();
                String musicId = currentTrackList.getId();
                trackInfo.put("music_title",currentTrackList.getName());
                trackInfo.put("music_url",currentTrackList.getSpotifyLink());

                mPlaylist.child(id).child("tracklist").child(musicId).updateChildren(trackInfo);
            }
        }

        // Save Concert Area

        DatabaseReference mArea = mDatabase.child("Area").child(concertKey);

        for(Area area : areaList){
            Map areaInfo = new HashMap();

            areaInfo.put("area_name",area.getAreaName());
            areaInfo.put("area_price", area.getAreaPrice());
            areaInfo.put("area_ticket_amount", area.getTicketAmount());

            mArea.child("area_detail").push().updateChildren(areaInfo);
        }

        if(areaImageUri!= null){
            Log.d("areaImageNotNull", "in");
            final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("areaImages").child(concertKey);

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(),areaImageUri);
            } catch (IOException s) {
                s.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = filepath.putBytes(data);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUrl) {
                            Map userInfo = new HashMap();

                            userInfo.put("areaImageUrl", downloadUrl.toString());

                            Log.d("areaImageUploaded", "in");

                            mDatabase.child("Concerts").child(concertKey).updateChildren(userInfo);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("uploadFail",e.getMessage().toString());
                        }
                    });
                }
            });
        } else {
            Log.d("resultUri", "out");
            return false;
        }

        // Save address Detail

        String place = placeInput.getText().toString();
        String province = provinceInput.getText().toString();
        String city = cityInput.getText().toString();
        String desc = descInput.getText().toString();
        String address = addressInput.getText().toString();

        Log.d("cek Key", concertKey);

        DatabaseReference addressDatabase = mDatabase.child("Address").child(concertKey);

        final Map placeInfo = new HashMap();
        placeInfo.put("place", place);
        placeInfo.put("province", province);
        placeInfo.put("city",city);
        placeInfo.put("address",address);
        placeInfo.put("desc",desc);

        // Database Reference
        addressDatabase.updateChildren(placeInfo);

        Toast.makeText(this, "Concert is successfully added!", Toast.LENGTH_SHORT).show();

        return true;
    }

    public void callNextScreen() {

        if(!savePlaceInformation()){
            return;
        }

        Intent intent = new Intent(getApplicationContext(), RetailerDashboard.class);

        startActivity(intent);

    }


}