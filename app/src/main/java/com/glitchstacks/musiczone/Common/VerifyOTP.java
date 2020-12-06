package com.glitchstacks.musiczone.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;

import com.chaos.view.PinView;
import com.glitchstacks.musiczone.R;

public class VerifyOTP extends AppCompatActivity {


    PinView pinFromUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_verify_o_t_p);

        //hooks
        pinFromUser = findViewById(R.id.pin_view);
        String _phoneNo = getIntent().getStringExtra("phoneNo");
        sendVerificationCodeToUser(_phoneNo);

    }

    private void sendVerificationCodeToUser(String phoneNo) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }
}