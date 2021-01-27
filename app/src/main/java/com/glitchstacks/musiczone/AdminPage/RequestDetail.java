package com.glitchstacks.musiczone.AdminPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.glitchstacks.musiczone.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RequestDetail extends AppCompatActivity {

    private ImageView userIdentity;
    private TextView bank_acc;
    private Button permitButton;
    private Button notPermitButton;
    String userID;
    private DatabaseReference mDatabase;
    private ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);

        userIdentity = findViewById(R.id.imageIdentity);
        bank_acc = findViewById(R.id.bank_acc);
        permitButton = findViewById(R.id.permitButton);
        notPermitButton = findViewById(R.id.notPermitButton);
        back_btn = findViewById(R.id.back_btn);

        userID = getIntent().getStringExtra("userID");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        fillInformation();

        permitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permitRequest();
                finish();
            }
        });

        notPermitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deniedRequest();
                finish();
            }
        });
    }

    private void fillInformation() {

        mDatabase.child("Request").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    String bank = snapshot.child("bank_acc").getValue().toString();
                    bank_acc.setText(bank);

                    String identity_url = snapshot.child("identity_url").getValue().toString();

                    try {
                        Glide.clear(userIdentity);
                        Glide.with(getApplication()).load(identity_url).into(userIdentity);
                    }catch(Exception e){

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void deniedRequest() {
        mDatabase.child("Request").child(userID).child("accepted").setValue("false");
    }

    private void permitRequest() {

        mDatabase.child("Users").child(userID).child("promotor").setValue("true");
        mDatabase.child("Users").child(userID).child("balance").setValue("0");
        mDatabase.child("Users").child(userID).child("bank_acc").setValue(bank_acc.getText().toString());

        mDatabase.child("Request").child(userID).removeValue();
    }
}