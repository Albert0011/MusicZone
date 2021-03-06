package com.glitchstacks.musiczone.Entries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.glitchstacks.musiczone.Profile.SetNewPassword;
import com.glitchstacks.musiczone.Profile.UserHelperClass;
import com.glitchstacks.musiczone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VerifyOTP extends AppCompatActivity {

    private FirebaseAuth mAuth;
    String codeBySystem, whatToDo, phoneNo;
    PinView pinFromUser;
    TextView phoneLayout;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private void verifyCode(String code) {

        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);
            if(whatToDo.equals("updateData")){
                updateOldUserData();
            } else {
                createNewUser();
            }
            //signInWithPhoneAuthCredential(credential);
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, "Verification Code is wrong", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }

    private void updateOldUserData() {
        Intent intent = new Intent(getApplicationContext(), SetNewPassword.class);
        intent.putExtra("phoneNo", phoneNo);
        startActivity(intent);
        finish();
    }


//    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//
//        try{
//            mAuth.signInWithCredential(credential)
//                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//
//                                Toast.makeText(VerifyOTP.this, "Verification Completed!", Toast.LENGTH_SHORT).show();
//
//                                try{
//                                    createNewUser();
//                                }catch (Exception i){
//                                    Toast.makeText(VerifyOTP.this, i.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//
//                            } else {
//
//                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                    Toast.makeText(VerifyOTP.this, "Verification Failed! Try Again!", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        }
//
//
//                    });
//        }catch(Exception e){
//            Toast toast = Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
//        }
//    }

    private void createNewUser() {

        String fullname = getIntent().getStringExtra("fullname");
        String email = getIntent().getStringExtra("email");
        String username = getIntent().getStringExtra("username");
        String password = getIntent().getStringExtra("password");
        String date = getIntent().getStringExtra("date");
        String gender = getIntent().getStringExtra("gender");
        String phoneNo = getIntent().getStringExtra("phoneNo");

//        Toast.makeText(VerifyOTP.this, username+password+date+gender+phoneNo+fullname, Toast.LENGTH_SHORT).show();

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Users");

        UserHelperClass addNewUser = new UserHelperClass(fullname, username, email, phoneNo, password, date, gender);

        reference.child(phoneNo).setValue(addNewUser);
        reference.child(phoneNo).child("promotor").setValue("false");
        reference.child(phoneNo).child("description").setValue("Hi it is me!");

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Toast.makeText(VerifyOTP.this, "Authentication succeeded, Now Login.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(VerifyOTP.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });


        startActivity(new Intent(VerifyOTP.this, Login.class));
        finish();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_verify_o_t_p);

        mAuth = FirebaseAuth.getInstance();
        mCallbacks =
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        Toast.makeText(VerifyOTP.this, "Code is sent!", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(VerifyOTP.this, s+" is in message", Toast.LENGTH_SHORT).show();
                        codeBySystem = s;
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                        String code = phoneAuthCredential.getSmsCode();

                        Toast.makeText(VerifyOTP.this, code, Toast.LENGTH_SHORT).show();

                        if (code != null) {
                            pinFromUser.setText(code);
//                            verifyCode(code);
                        }
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(VerifyOTP.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                };

        //hooks
        phoneLayout = findViewById(R.id.phone_layout);
        pinFromUser = findViewById(R.id.pin_view);
        phoneNo = getIntent().getStringExtra("phoneNo");
        whatToDo = getIntent().getStringExtra("whatToDo");

        phoneLayout.setText("Enter one time password sent on " + phoneNo);

        sendVerificationCodeToUser(phoneNo);

    }

    private void sendVerificationCodeToUser(String phoneNo) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNo)       // Phone number to verify
                        .setTimeout(5L, TimeUnit.SECONDS) // Timeout and unit
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