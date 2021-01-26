package com.glitchstacks.musiczone.Dashboard;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.glitchstacks.musiczone.Database.SessionManager;
import com.glitchstacks.musiczone.Entries.IntroductoryActivity;
import com.glitchstacks.musiczone.Entries.Login;
import com.glitchstacks.musiczone.PostConcert.AddConcert;
import com.glitchstacks.musiczone.Dashboard.DashboardFragment.ChatDashboardFragment;
import com.glitchstacks.musiczone.Dashboard.DashboardFragment.ExploreDashboardFragment;
import com.glitchstacks.musiczone.Dashboard.DashboardFragment.ProfileFragment;
import com.glitchstacks.musiczone.Dashboard.DashboardFragment.TicketDashboardFragment;
import com.glitchstacks.musiczone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.HashMap;

public class RetailerDashboard extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;
    FirebaseAuth mAuth;
    String phoneNumber, promotor;
    DatabaseReference mDatabase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.retailer_dashboard);

        SessionManager sessionManager = new SessionManager(RetailerDashboard.this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> userDetails = sessionManager.getUsersDetailFromSession();

        String fullName = userDetails.get(SessionManager.KEY_FULLNAME);
        phoneNumber = userDetails.get(SessionManager.KEY_PHONENUMBER);
        String gender = userDetails.get(SessionManager.KEY_GENDER);
        String email = userDetails.get(SessionManager.KEY_EMAIL);
        String password = userDetails.get(SessionManager.KEY_PASSWORD);
        String date = userDetails.get(SessionManager.KEY_DATE);
        String username = userDetails.get(SessionManager.KEY_USERNAME);
        promotor = userDetails.get(SessionManager.KEY_PROMOTOR);
        Log.d("cekPromotor",promotor.toString());

        mDatabase = FirebaseDatabase.getInstance().getReference();


        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){ // Email and password match
                        Log.d("retailerDashboard", "loginaccepted");

                    } else {
                        // Set the error message
                        Log.d("retailerDashboard", "logindenied");

                    }
                }
            });

        chipNavigationBar = findViewById(R.id.bottom_nav_menu);
        chipNavigationBar.setItemSelected(R.id.bottom_nav_explore, true);
        if(promotor.equals("false")){
            chipNavigationBar.setItemEnabled(R.id.bottom_nav_post, false);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ExploreDashboardFragment()).commit();
        bottomMenu();
    }

    private void bottomMenu() {

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {

                Fragment fragment = null;

                switch (i){

                    case R.id.bottom_nav_me:
                        fragment = new ProfileFragment();
                        break;
                    case R.id.bottom_nav_chat:
                        fragment = new ChatDashboardFragment();
                        break;
                    case R.id.bottom_nav_explore:
                        fragment = new ExploreDashboardFragment();
                        break;
                    case R.id.bottom_nav_ticket:
                        fragment = new TicketDashboardFragment();
                        break;
                    case R.id.bottom_nav_post:
                        fragment = new AddConcert();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

            }
        });

    }

    public void callLoginScreen(View view){

        Intent intent = new Intent(getApplicationContext(), Login.class);

        Pair[] pairs = new Pair[1];

        pairs[0] = new Pair<View, String>(findViewById(R.id.app_name),"transition_signin_btn");

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RetailerDashboard.this, pairs);
            startActivity(intent,options.toBundle());
        }
        else{
            startActivity(intent);
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }



}
