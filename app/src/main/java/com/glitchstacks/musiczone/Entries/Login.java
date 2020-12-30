package com.glitchstacks.musiczone.Entries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.se.omapi.Session;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.glitchstacks.musiczone.Common.Chatroom;
import com.glitchstacks.musiczone.Common.MainChat;
import com.glitchstacks.musiczone.Common.MusicZoneStartUpScreen;
import com.glitchstacks.musiczone.Common.RetailerDashboard;
import com.glitchstacks.musiczone.Database.SessionManager;
import com.glitchstacks.musiczone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;


public class Login extends AppCompatActivity {

    TextInputLayout phoneNo, password;
    EditText phoneNumberEditText, passwordEditText;
    Button mLoginBtn;
    FirebaseAuth mAuth;
    ProgressDialog mProgress;
    //    DatabaseReference mDatabaseUsers;
    CountryCodePicker countryCodePicker;
    CheckBox rememberMe;

    private SessionManager sessionManagerLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_login);

        phoneNo = findViewById(R.id.username_input);
        password = findViewById(R.id.password_input);
        mLoginBtn = findViewById(R.id.login_button);
        countryCodePicker = findViewById(R.id.country_picker);
        rememberMe = findViewById(R.id.remember_me);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        SessionManager sessionManager = new SessionManager(Login.this, SessionManager.SESSION_REMEMBERME);
        if(sessionManager.checkRememberMe()){
            HashMap<String, String> rememberMeDetails = sessionManager.getRememberMeDetailFromSession();
            phoneNumberEditText.setText(rememberMeDetails.get(SessionManager.KEY_SESSIONPHONENUMBER));
            passwordEditText.setText(rememberMeDetails.get(SessionManager.KEY_SESSIONPASSWORD));

        }

        mAuth = FirebaseAuth.getInstance();
//        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
//        mDatabaseUsers.keepSynced(true);  //To keep it offline
        mProgress = new ProgressDialog(this);

        // Set up the buttons
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLogin();
            }
        });

    }

    private void checkLogin() {

        //Toast.makeText(Login.this, "masuk1", Toast.LENGTH_LONG).show();

        if(!isConnected(this)){
            showCustomDialog();
            return;
        }

        // Get the text of email and password from EditText
//        Toast.makeText(Login.this, _username+_password, Toast.LENGTH_SHORT).show();

        if(rememberMe.isChecked()){
            SessionManager sessionManager = new SessionManager(Login.this, SessionManager.SESSION_REMEMBERME);
            String _getUserEnteredPhoneNumber = phoneNo.getEditText().getText().toString().trim();
            sessionManager.createRememberMeSession(_getUserEnteredPhoneNumber, password.getEditText().getText().toString());
        }

        if(!validatePassword() | !validatePhoneNo()){
            return;
        } else {

            // Show the progress dialog
            mProgress.setMessage("Checking Login ...");
            mProgress.show();

            //Toast.makeText(Login.this, "masuk1", Toast.LENGTH_LONG).show();
            checkUserExist();

            // Start signing in
//            mAuth.signInWithEmailAndPassword(_email, _password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//
//                    if (task.isSuccessful()){ // Email and password match
//
//                        mProgress.dismiss();
//                        checkUserExist(); // Check if first user or not
//
//                    } else {
//
//                        // Set the error message
//                        mProgress.dismiss();
//                        Toast.makeText(Login.this, "Email or Password did not match!", Toast.LENGTH_SHORT).show();
//
//                    }
//                }
//            });

        }

    }

    private void showCustomDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setMessage("Connect to the internet first")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private boolean isConnected(Login login) {

        ConnectivityManager cm = (ConnectivityManager) login.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network nw = cm.getActiveNetwork();
            if (nw == null) return false;
            NetworkCapabilities actNw = cm.getNetworkCapabilities(nw);
            return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
        } else {
            NetworkInfo nwInfo = cm.getActiveNetworkInfo();
            return nwInfo != null && nwInfo.isConnected();
        }

    }

    private boolean validatePhoneNo() {
        String val = phoneNo.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            phoneNo.setError("Field cannot be empty");
            return false;
        }

        return true;

    }

    private boolean validatePassword(){
        String val = password.getEditText().getText().toString().trim();
        String checkPassword = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                //"(?=.*[a-zA-Z])" +      //any letter
                //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            password.setError("Field can not be empty");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    private void checkUserExist() {

        //FirebaseUser user = mAuth.getCurrentUser();
        //startActivity(new Intent(Login.this, MainChat.class));
//        if (user != null){
//            //Check the user's UID is in the database or not
//            final String userId = mAuth.getCurrentUser().getUid(); //Get the current user's UID
//            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.hasChild(userId)) { //Database has the current user's UID value
//
//                        //Go to MainActivity
//                        Toast.makeText(Login.this, "Login Success!", Toast.LENGTH_SHORT).show();
//                        mProgress.dismiss();
//                        finish();
//                        startActivity(new Intent(Login.this, RetailerDashboard.class));
//
//                    } else {
//
//                        Toast.makeText(Login.this, "User does not exist", Toast.LENGTH_SHORT).show();
//
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        }


        String _getUserEnteredPhoneNumber = phoneNo.getEditText().getText().toString().trim();
        final String _phoneNo = "+"+countryCodePicker.getSelectedCountryCode()+_getUserEnteredPhoneNumber;

        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNo").equalTo(_phoneNo);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(Login.this, "masuk2", Toast.LENGTH_LONG).show();
                if(snapshot.exists()){
                    phoneNo.setError(null);
                    phoneNo.setErrorEnabled(false);
                    String pass = snapshot.child(_phoneNo).child("password").getValue(String.class);

                    if(pass.equals(password.getEditText().getText().toString())){
                        password.setError(null);
                        password.setErrorEnabled(false);
                        mProgress.dismiss();

                        //Toast.makeText(Login.this, "masuk3", Toast.LENGTH_LONG).show();
                        String _fullname = snapshot.child(_phoneNo).child("fullname").getValue(String.class);
                        String _username = snapshot.child(_phoneNo).child("username").getValue(String.class);
                        String _date = snapshot.child(_phoneNo).child("date").getValue(String.class);
                        String _email = snapshot.child(_phoneNo).child("email").getValue(String.class);
                        String _gender = snapshot.child(_phoneNo).child("gender").getValue(String.class);
                        String _password = snapshot.child(_phoneNo).child("pasword").getValue(String.class);

//                        Toast.makeText(Login.this, _username, Toast.LENGTH_LONG).show();

                        sessionManagerLogin = new SessionManager(Login.this, SessionManager.SESSION_USERSESSION);
                        sessionManagerLogin.createLoginSession(_fullname,_username,_email, _phoneNo, _password, _gender, _date);

                        Intent intent = new Intent(getApplicationContext(), RetailerDashboard.class);
                        startActivity(intent);
                        finish();


                    }else{
                        mProgress.dismiss();
                        Toast.makeText(Login.this, "Your password is wrong!", Toast.LENGTH_LONG).show();
                    }
                }else{
                    mProgress.dismiss();
                    phoneNo.setError("Such user did not exists.");
                    Toast.makeText(Login.this, "Phone number not recognized!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    public void callSignUpScreen(View view){

        Intent intent = new Intent(getApplicationContext(), SignUp.class);

        Pair[] pairs = new Pair[1];

        pairs[0] = new Pair<View, String>(findViewById(R.id.btn_sign_up),"transition_signup_btn");

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
            startActivity(intent,options.toBundle());
        }
        else{
            startActivity(intent);
        }
    }

    public void backToBefore(View view){

        Intent intent = new Intent(getApplicationContext(), MusicZoneStartUpScreen.class);
        startActivity(intent);

    }

    public SessionManager getSession(){
        return sessionManagerLogin;
    }
}