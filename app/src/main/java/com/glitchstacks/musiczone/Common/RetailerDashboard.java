package com.glitchstacks.musiczone.Common;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.glitchstacks.musiczone.Database.SessionManager;
import com.glitchstacks.musiczone.Entries.Login;
import com.glitchstacks.musiczone.LocationOwner.ChatDashboardFragment;
import com.glitchstacks.musiczone.LocationOwner.ExploreDashboardFragment;
import com.glitchstacks.musiczone.LocationOwner.ProfileFragment;
import com.glitchstacks.musiczone.LocationOwner.TicketDashboardFragment;
import com.glitchstacks.musiczone.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.HashMap;

public class RetailerDashboard extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.retailer_dashboard);

        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> userDetails = sessionManager.getUsersDetailFromSession();

        String fullName = userDetails.get(SessionManager.KEY_FULLNAME);
        String phoneNumber = userDetails.get(SessionManager.KEY_PHONENUMBER);
        String gender = userDetails.get(SessionManager.KEY_GENDER);
        String email = userDetails.get(SessionManager.KEY_EMAIL);
        String password = userDetails.get(SessionManager.KEY_PASSWORD);
        String date = userDetails.get(SessionManager.KEY_DATE);
        String username = userDetails.get(SessionManager.KEY_USERNAME);

        Toast.makeText(RetailerDashboard.this, username, Toast.LENGTH_LONG).show();

        chipNavigationBar = findViewById(R.id.bottom_nav_menu);
        chipNavigationBar.setItemSelected(R.id.bottom_nav_explore, true);
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
}
