package com.glitchstacks.musiczone.BuyTicket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.glitchstacks.musiczone.Database.SessionManager;
import com.glitchstacks.musiczone.R;
import com.glitchstacks.musiczone.WaitConfirmationActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {

    private TextView amount, fee, total, virtualNum;
    private Button btnNext;
    private DatabaseReference mDatabase;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        SessionManager sessionManager = new SessionManager(PaymentActivity.this, SessionManager.SESSION_USERSESSION);

        // User Information map
        HashMap<String, String> map = sessionManager.getUsersDetailFromSession();

        // UserID
        phoneNumber = map.get(SessionManager.KEY_PHONENUMBER);

        // DatabaseReference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        amount = findViewById(R.id.amount_layout);
        fee = findViewById(R.id.fee);
        total = findViewById(R.id.total);
        virtualNum = findViewById(R.id.virtual_num);

        btnNext = findViewById(R.id.btnNext);

        loadData();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callNextScreen();
            }
        });

    }

    private void callNextScreen() {

        saveData();
        Intent intent = new Intent(getApplicationContext(), WaitConfirmationActivity.class);

        startActivity(intent);

    }

    private void saveData(){

        DatabaseReference ticketDatabase = mDatabase.child("Users").child(phoneNumber).child("tickets");
        final String concertKey = getIntent().getStringExtra("concertKey");
        String amountTicket = getIntent().getStringExtra("amountTicket");
        String area = getIntent().getStringExtra("area");

        final Map ticketInfo = new HashMap();
        ticketInfo.put("concert_key", concertKey);
        ticketInfo.put("amountTicket", amountTicket);
        ticketInfo.put("area", area);
        ticketInfo.put("status", false);

        // Database Reference
        ticketDatabase.push().updateChildren(ticketInfo);

    }

    private void loadData(){

        String totalPurchase = getIntent().getStringExtra("totalPurchase");

        Log.d("total",totalPurchase);
        Integer totalPurchaseInt = Integer.parseInt(totalPurchase) + 2000;

        amount.setText("IDR " + totalPurchase);
        fee.setText("IDR 2000");

        total.setText("IDR " + totalPurchaseInt.toString());

    }

}