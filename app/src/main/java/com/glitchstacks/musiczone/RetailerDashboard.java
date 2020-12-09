package com.glitchstacks.musiczone;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.glitchstacks.musiczone.Common.Login;
import com.glitchstacks.musiczone.HelperClasses.ExplorePageAdapter.FeaturedAdapter;
import com.glitchstacks.musiczone.HelperClasses.ExplorePageAdapter.FeaturedHelperClass;
import com.glitchstacks.musiczone.LocationOwner.ChatDashboardFragment;
import com.glitchstacks.musiczone.LocationOwner.ExploreDashboardFragment;
import com.glitchstacks.musiczone.LocationOwner.ProfileFragment;
import com.glitchstacks.musiczone.LocationOwner.TicketDashboardFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;

public class RetailerDashboard extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.retailer_dashboard);

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
