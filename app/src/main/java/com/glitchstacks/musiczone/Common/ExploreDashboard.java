package com.glitchstacks.musiczone.Common;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.glitchstacks.musiczone.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class ExploreDashboard extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.retailer_dashboard);

        chipNavigationBar = findViewById(R.id.bottom_nav_menu);
        bottomMenu();

    }

    private void bottomMenu() {



    }
}
