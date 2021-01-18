package com.glitchstacks.musiczone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.glitchstacks.musiczone.Common.ExploreDashboard;
import com.glitchstacks.musiczone.Common.RetailerDashboard;
import com.glitchstacks.musiczone.Database.SessionManager;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    private void savePlaceInformation() {

        Boolean a,b,c,d,e;

        a = isAddressValid();
        b = isPlaceValid();
        c = isCityValid();
        d = isDescValid();
        e = isProvinceValid();

        if(!a || !b || !c || !d || !e){
            return;
        }

        String playlistName = getIntent().getStringExtra("playlistName");
        String key = getIntent().getStringExtra("key");

        String place = placeInput.getText().toString();
        String province = provinceInput.getText().toString();
        String city = cityInput.getText().toString();
        String desc = descInput.getText().toString();
        String address = addressInput.getText().toString();

        Log.d("cek Key", key);
        Log.d("cek PlaylistName", playlistName);

        DatabaseReference addressDatabase = mDatabase.child("Address").child(key);

        final Map placeInfo = new HashMap();
        placeInfo.put("place", place);
        placeInfo.put("province", province);
        placeInfo.put("city",city);
        placeInfo.put("address",address);
        placeInfo.put("desc",desc);

        // Database Reference
        final String key2 = addressDatabase.push().getKey();
        addressDatabase.child(key2).updateChildren(placeInfo);
        Toast.makeText(this, "Address is successfully added!", Toast.LENGTH_SHORT).show();

    }

    public void callNextScreen() {

        savePlaceInformation();

        Intent intent = new Intent(getApplicationContext(), RetailerDashboard.class);

        startActivity(intent);

    }


}