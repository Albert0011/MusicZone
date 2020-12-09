package com.glitchstacks.musiczone.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.glitchstacks.musiczone.R;
import com.glitchstacks.musiczone.TicketDashboard;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Login extends AppCompatActivity {

    EditText username, password;
    Button mLoginBtn;
    FirebaseAuth mAuth;
    ProgressDialog mProgress;
    DatabaseReference mDatabaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_login);

        username = findViewById(R.id.username_input);
        password = findViewById(R.id.password_input);
        mLoginBtn = findViewById(R.id.login_button);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);  //To keep it offline
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

        // Get the text of email and password from EditText
        String _username = username.getText().toString();
        String _password = password.getText().toString();
        Toast.makeText(Login.this, _username+_password, Toast.LENGTH_SHORT).show();

        if (TextUtils.isEmpty(_username)){ // Empty email field

            // Set the error message
            Toast.makeText(Login.this, "Username is empty", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(_password)){ //Empty password field

            // Set the error message
            Toast.makeText(Login.this, "Password is empty", Toast.LENGTH_SHORT).show();

        } else {

            // Show the progress dialog
            mProgress.setMessage("Checking Login ...");
            mProgress.show();

            // Start signing in
            mAuth.signInWithEmailAndPassword(_username, _password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){ // Email and password match

                        checkUserExist(); // Check if first user or not

                    } else {

                        // Set the error message
                        mProgress.dismiss();
                        Toast.makeText(Login.this, "Error login", Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }

    }

    private void checkUserExist() {

        if (mAuth.getCurrentUser() != null){

            //Check the user's UID is in the database or not
            final String userId = mAuth.getCurrentUser().getUid(); //Get the current user's UID
            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(userId)) { //Database has the current user's UID value

                        //Go to MainActivity
                        mProgress.dismiss();
                        finish();
                        startActivity(new Intent(Login.this, TicketDashboard.class));

                    } else {

                        Toast.makeText(Login.this, "User does not exist", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
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
}