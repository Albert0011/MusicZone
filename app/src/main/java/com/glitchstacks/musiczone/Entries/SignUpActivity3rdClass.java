package com.glitchstacks.musiczone.Entries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.Toast;

import com.glitchstacks.musiczone.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class SignUpActivity3rdClass extends AppCompatActivity {

    //Variables
    TextInputLayout phoneNumberLayout;
    ScrollView scrollView;
    TextInputEditText phoneNumber;
    CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up_activity3rd_class);

        //Hooks

        scrollView = findViewById(R.id.signup_scrollview);
        countryCodePicker = findViewById(R.id.signup_country_picker);
        phoneNumber = findViewById(R.id.signup_phone_number);
        phoneNumberLayout = findViewById(R.id.phoneNumberLayout);


    }

    public void backToBefore(View view){

        Intent intent = new Intent(getApplicationContext(), SignUp2ndClass.class);

        Pair[] pairs = new Pair[1];

        pairs[0] = new Pair<View, String>(findViewById(R.id.signup_back_btn),"transition_back_btn");

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpActivity3rdClass.this, pairs);
            startActivity(intent,options.toBundle());
        }
        else{
            startActivity(intent);
        }
    }

    public void callVerifyOTPScreen(View view){

        if(!validatePhoneNumber()){
            return;
        }



        String _getUserEnteredPhoneNumber = phoneNumber.getText().toString().trim();
        String _phoneNo = "+"+countryCodePicker.getSelectedCountryCode()+_getUserEnteredPhoneNumber;

        checkUserExist(_phoneNo);


    }

    private void checkUserExist(final String phoneNo) {

        Toast.makeText(SignUpActivity3rdClass.this, phoneNo, Toast.LENGTH_LONG).show();

        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNo").equalTo(phoneNo);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(SignUpActivity3rdClass.this, "masuk2", Toast.LENGTH_LONG).show();
                if(snapshot.exists()) {
                    phoneNumberLayout.setError("This number is already exists.");
                    phoneNumberLayout.setErrorEnabled(true);
                }
                else{
                    phoneNumberLayout.setError(null);
                    phoneNumberLayout.setErrorEnabled(false);

                    //Get values from previous screen
                    String _fullname = getIntent().getStringExtra("fullname");
                    String _email = getIntent().getStringExtra("email");
                    String _username = getIntent().getStringExtra("username");
                    String _password = getIntent().getStringExtra("password");
                    String _date = getIntent().getStringExtra("date");
                    String _gender = getIntent().getStringExtra("gender");

                    Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);

                    //Pass all fields to the next activity
                    intent.putExtra("fullname", _fullname);
                    intent.putExtra("email", _email);
                    intent.putExtra("username", _username);
                    intent.putExtra("password", _password);
                    intent.putExtra("date", _date);
                    intent.putExtra("gender", _gender);
                    intent.putExtra("phoneNo", phoneNo);
                    intent.putExtra("whatToDo","nonono");
                    //intent.putExtra("whatToDO", "createNewUser"); // This is to identify that which action should OTP perform after verification.

                    //Add Transition
                    Pair[] pairs = new Pair[1];
                    pairs[0] = new Pair<View, String>(scrollView, "transition_OTP_screen");
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpActivity3rdClass.this, pairs);
                        startActivity(intent, options.toBundle());
                    } else {
                        startActivity(intent);
                    }
                    finish();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }


    //ini gua buat sendiri di tutor g ada
    private boolean validatePhoneNumber(){

        String val = phoneNumber.getText().toString().trim();

        if (val.isEmpty()) {
            phoneNumber.setError("Field cannot be empty");
            return false;
        }

        return true;
    }

    public void callLoginScreen(View view){

        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);

    }

}