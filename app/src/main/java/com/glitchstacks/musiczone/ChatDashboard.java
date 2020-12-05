package com.glitchstacks.musiczone;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.glitchstacks.musiczone.LocationOwner.ChatDashboardFragment;
import com.glitchstacks.musiczone.LocationOwner.ExploreDashboardFragment;
import com.glitchstacks.musiczone.LocationOwner.TicketDashboardFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class ChatDashboard extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ticket);

        chipNavigationBar = findViewById(R.id.bottom_nav_menu);
        chipNavigationBar.setItemSelected(R.id.bottom_nav_chat, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChatDashboardFragment()).commit();
        bottomMenu();

    }

    private void bottomMenu() {

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {

                Fragment fragment = null;

                switch (i){

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
}
