package com.glitchstacks.musiczone.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.glitchstacks.musiczone.R;
import com.glitchstacks.musiczone.TicketDashboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VerifyOTP extends AppCompatActivity {

    private FirebaseAuth mAuth;
    String codeBySystem;
    PinView pinFromUser;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    codeBySystem = s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        pinFromUser.setText(code);
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(VerifyOTP.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };

    private void verifyCode(String code) {

        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);
            signInWithPhoneAuthCredential(credential);
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, "Verification Code is wrong", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        try{
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(VerifyOTP.this, "Verification Completed!", Toast.LENGTH_SHORT).show();

                                try{
                                    createNewUser();
                                }catch (Exception i){
                                    Toast.makeText(VerifyOTP.this, i.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            } else {

                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(VerifyOTP.this, "Verification Failed! Try Again!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }


                    });
        }catch(Exception e){
            Toast toast = Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private void createNewUser() {

        String fullname = getIntent().getStringExtra("fullname");
        String email = getIntent().getStringExtra("email");
        String username = getIntent().getStringExtra("username");
        String password = getIntent().getStringExtra("password");
        String date = getIntent().getStringExtra("date");
        String gender = getIntent().getStringExtra("gender");
        String phoneNo = getIntent().getStringExtra("phoneNo");

        Toast.makeText(VerifyOTP.this, username+password+date+gender+phoneNo+fullname, Toast.LENGTH_SHORT).show();

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Users");

        reference.setValue("FirstRecord!");

        UserHelperClass addNewUser = new UserHelperClass(fullname, username, email, phoneNo, password, date, gender);

        reference.child(phoneNo).setValue(addNewUser);

//        startActivity(new Intent(getApplicationContext(), TicketDashboard.class));
//        finish();


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_verify_o_t_p);

        mAuth = FirebaseAuth.getInstance();
        //hooks
        pinFromUser = findViewById(R.id.pin_view);

        String fullname = getIntent().getStringExtra("fullname");
        String email = getIntent().getStringExtra("email");
        String username = getIntent().getStringExtra("username");
        String password = getIntent().getStringExtra("password");
        String date = getIntent().getStringExtra("date");
        String gender = getIntent().getStringExtra("gender");
        String phoneNo = getIntent().getStringExtra("phoneNo");

        sendVerificationCodeToUser(phoneNo.toString());

    }

    private void sendVerificationCodeToUser(String phoneNo) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNo)       // Phone number to verify
                        .setTimeout(10L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(VerifyOTP.this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    public void callNextScreenFromOTP(View view) {

        String code = pinFromUser.getText().toString();

        verifyCode(code);

    }
}