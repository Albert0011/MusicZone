package com.glitchstacks.musiczone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.glitchstacks.musiczone.Database.SessionManager;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BuyTicket extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private DatabaseReference mDatabase;
    private ArrayList<String> arrayList, arrayPrice;
    private EditText amountInput;
    private TextInputLayout amountLayout;
    private TextView txtArea, txtTotal;
    private String phoneNumber, imageURL, amount, currentArea;
    private Button btnNext, btnUpdate;
    private ImageView areaImage;
    private Integer currentPrice, totalPurchase;

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket);

        currentPrice=0;

        // Session Manager for Current User
        SessionManager sessionManager = new SessionManager(getApplicationContext(), SessionManager.SESSION_USERSESSION);

        // User Information map
        HashMap<String, String> map = sessionManager.getUsersDetailFromSession();

        // UserID
        phoneNumber = map.get(SessionManager.KEY_PHONENUMBER);

        // TextInputLayouts
        amountLayout = findViewById(R.id.amount_layout);

        // EditText
        amountInput = findViewById(R.id.amount_input);

        // Button
        btnNext = findViewById(R.id.btnNext);
        btnUpdate = findViewById(R.id.btnUpdate);

        // TextView
        txtArea = findViewById(R.id.txtArea);
        txtTotal = findViewById(R.id.txtTotal);

        // DatabaseReference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Image
        areaImage = findViewById(R.id.imgArea);

        // ArrayList
        arrayList = new ArrayList<>();
        arrayPrice = new ArrayList<>();

        arrayList.add("Select Item");


        // OnClick Listener for Next Button
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callNextScreen();
            }
        });

        loadAreaInformation();

        spinner = (Spinner)findViewById(R.id.spinner_area);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(BuyTicket.this, android.R.layout.simple_spinner_item,arrayList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });


    }

    private Boolean isItemValid() {
        if(txtArea.getText().toString().equals("Select Item")){
            Toast.makeText(this, "You need to choose an area!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private Boolean isAmountValid() {

        amount = amountInput.getText().toString();

        if(amount.isEmpty()){
            amountLayout.setError("field can't be empty");
            return false;
        }

        Integer amountInt = Integer.parseInt(amount);

        if(amountInt < 1){
            amountLayout.setError("amount can't go lower than 1");
            return false;
        }
        else{
            if(amountInt > 4){
                amountLayout.setError("Too many tickets!");
                return false;
            } else{
                amountLayout.setError(null);
                amountLayout.setErrorEnabled(false);
            }
        }
        return true;
    }

    private void validate() {

        Boolean a,b;
        a = isItemValid();
        b = isAmountValid();

        if(!a || !b){
            return;
        }

    }

    private void updateData() {

        validate();

        totalPurchase = Integer.parseInt(amountInput.getText().toString()) * currentPrice;

        Log.d("cek totalPurchase", totalPurchase.toString());
        Log.d("cek currentPrice", currentPrice.toString());
        Log.d("cek amountInp", amountInput.getText().toString());
        txtTotal.setText("IDR " + totalPurchase.toString());

    }

    private void callNextScreen() {

        updateData();

        final String concertKey = getIntent().getStringExtra("concertKey");

        totalPurchase = Integer.parseInt(amountInput.getText().toString()) * currentPrice;
        String totalPurch = totalPurchase.toString();

        Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
        intent.putExtra("concertKey",concertKey);
        intent.putExtra("totalPurchase", totalPurch);
        intent.putExtra("amountTicket", amountInput.getText().toString());
        intent.putExtra("area", txtArea.getText().toString());

        startActivity(intent);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        txtArea.setText(arrayList.get(position));
        currentArea = arrayList.get(position);

        if(position==0){
            currentPrice = 0;
        } else{
            currentPrice = Integer.parseInt(arrayPrice.get(position-1));
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

    private void loadAreaInformation() {

        final String key = getIntent().getStringExtra("concertKey");

        DatabaseReference mAreas = mDatabase.child("Areas").child(key);

        mAreas.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {

                    Map<String, Object> map = (Map<String, Object>) snapshot.child("area_image").getValue();
                    if (map.get("imageURL") != null) {
                        imageURL = map.get("imageURL").toString();
                        switch (imageURL) {
                            case "default":
                                Glide.with(getApplication()).load(R.mipmap.ic_launcher).into(areaImage);
                                break;
                            default:
                                Glide.clear(areaImage);
                                Glide.with(getApplication()).load(imageURL).into(areaImage);

                                break;
                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mAreas.child("area_detail").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                arrayList.add(snapshot.child("area_name").getValue().toString());
                arrayPrice.add(snapshot.child("area_price").getValue().toString());
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