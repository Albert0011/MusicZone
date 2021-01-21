package com.glitchstacks.musiczone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.glitchstacks.musiczone.Dashboard.RetailerDashboard;

public class WaitConfirmationActivity extends AppCompatActivity {

    Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_confirmation);

        confirm =findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callNextScreen();
            }
        });

    }

    private void callNextScreen() {

        Intent intent = new Intent(getApplicationContext(), RetailerDashboard.class);
        startActivity(intent);

    }
}