package com.glitchstacks.musiczone.BuyTicket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.glitchstacks.musiczone.Database.SessionManager;
import com.glitchstacks.musiczone.R;
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
    private ArrayList<String> arrayList, arrayPrice, arrayAmount, arrayKey;
    private EditText amountInput;
    private TextInputLayout amountLayout;
    private TextView txtArea, txtTotal, ticketAmountTxt;
    private String phoneNumber, imageURL, amount, currentArea, currentTicketAmount, currentKey;
    private Button btnNext, btnUpdate;
    private ImageView areaImage;
    private Integer currentPrice, totalPurchase, currentAmount;

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

        ticketAmountTxt = findViewById(R.id.ticketAmountTxt);


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
        arrayAmount = new ArrayList<>();
        arrayKey = new ArrayList<>();

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

        if(amount.equals("")){
            amountLayout.setError("field can't be empty");
            return false;
        }

        Integer amountInt = Integer.parseInt(amount);
        currentTicketAmount = ticketAmountTxt.getText().toString();
        currentAmount = Integer.parseInt(currentTicketAmount);

        if(amountInt > currentAmount){
            amountLayout.setError("ticket is not enough!");
            return false;
        }

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

    private boolean validate() {

        Boolean a,b;
        a = isItemValid();
        b = isAmountValid();

        if(!a || !b){
            return false;
        } else{
            return true;
        }

    }

    private void updateData() {

        if(!validate()){
            return;
        }

        totalPurchase = Integer.parseInt(amountInput.getText().toString()) * currentPrice;

        Log.d("cek totalPurchase", totalPurchase.toString());
        Log.d("cek currentPrice", currentPrice.toString());
        Log.d("cek amountInp", amountInput.getText().toString());
        txtTotal.setText("IDR " + totalPurchase.toString());

    }

    private void callNextScreen() {

        updateData();

        final String concertKey = getIntent().getStringExtra("concertKey");

        final DatabaseReference mAreas = mDatabase.child("Area").child(concertKey).child("area_detail").child(currentKey);

        Log.d("currentTicketAmount", currentTicketAmount);
        Log.d("currentAmount", currentAmount.toString());
        Integer amountInt = Integer.parseInt(amount);
        Integer ticketAmount = currentAmount;
        ticketAmount = ticketAmount - amountInt;
        mAreas.child("area_ticket_amount").setValue(ticketAmount.toString());

        totalPurchase = Integer.parseInt(amountInput.getText().toString()) * currentPrice;
        String totalPurch = totalPurchase.toString();

        Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
        intent.putExtra("concertKey",concertKey);
        intent.putExtra("totalPurchase", totalPurch);
        intent.putExtra("amountTicket", amountInput.getText().toString());
        intent.putExtra("area", txtArea.getText().toString());
        intent.putExtra("price", currentPrice.toString());

        startActivity(intent);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {


        Log.d("cek","cek masuk "+position);
        txtArea.setText(arrayList.get(position));
        currentArea = arrayList.get(position);

        if(position==0){
            currentPrice = 0;
            ticketAmountTxt.setText("0");
            currentKey = "0";
        } else{
            currentPrice = Integer.parseInt(arrayPrice.get(position-1));
            ticketAmountTxt.setText(arrayAmount.get(position-1));
            Log.d("array amount", arrayAmount.get(position-1));
            currentKey = arrayKey.get(position-1);
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

    private void loadAreaInformation() {

        final String key = getIntent().getStringExtra("concertKey");

        Log.d("ConcertKey", key);
        DatabaseReference mAreas = mDatabase.child("Area").child(key);
        DatabaseReference mAreaImage = mDatabase.child("Concerts").child(key);

        mAreaImage.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {

                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    if (map.get("areaImageUrl") != null) {
                        imageURL = map.get("areaImageUrl").toString();
                        Log.d("cek image", imageURL);
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

                if(snapshot.exists()){
                    Log.d("ceksnap",snapshot.child("area_name").getValue().toString());
                    arrayList.add(snapshot.child("area_name").getValue().toString());
                    arrayPrice.add(snapshot.child("area_price").getValue().toString());
                    arrayAmount.add(snapshot.child("area_ticket_amount").getValue().toString());
                    arrayKey.add(snapshot.getKey().toString());
                    Log.d("cek key",snapshot.getKey().toString());
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




}