package com.glitchstacks.musiczone.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.glitchstacks.musiczone.Entries.Login;
import com.glitchstacks.musiczone.R;

public class ForgetPasswordSuccessMessage extends AppCompatActivity {

    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_success_message);

        btnLogin = findViewById(R.id.loginBtn);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callNextScreen();
            }
        });

    }

    public void callNextScreen(){

        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();

    }

}