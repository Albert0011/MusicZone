package com.glitchstacks.musiczone.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.glitchstacks.musiczone.Entries.VerifyOTP;
import com.glitchstacks.musiczone.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ForgetPassword extends AppCompatActivity {

    TextInputLayout phoneNumberLayout;
    EditText phoneNumber;
    Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        phoneNumberLayout = findViewById(R.id.phoneNumberLayout);
        phoneNumber = findViewById(R.id.phoneNumberInput);
        nextBtn = findViewById(R.id.nextBtn);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyPhoneNumber();
            }
        });

    }

    public void verifyPhoneNumber(){

        final String _phoneNumber = phoneNumber.getText().toString().trim();
        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNo").equalTo(_phoneNumber);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

//                    phoneNumberLayout.setError(null);
//                    phoneNumberLayout.setErrorEnabled(false);

                    Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
                    intent.putExtra("phoneNo", _phoneNumber);
                    intent.putExtra("whatToDo", "updateData");
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(ForgetPassword.this, "No such user exists!", Toast.LENGTH_SHORT).show();
//                    phoneNumberLayout.setError("No such user exists!");
//                    phoneNumberLayout.requestFocus();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}