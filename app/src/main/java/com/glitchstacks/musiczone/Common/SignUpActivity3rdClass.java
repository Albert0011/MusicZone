package com.glitchstacks.musiczone.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import com.glitchstacks.musiczone.R;

public class SignUpActivity3rdClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_activity3rd_class);
    }

    public void backToBefore(View view){

        Intent intent = new Intent(getApplicationContext(), SignUp2ndClass.class);

        Pair[] pairs = new Pair[1];

        pairs[0] = new Pair<View, String>(findViewById(R.id.signup_back_btn),"transition_back_btn");

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpActivity3rdClass.this, pairs);
            startActivity(intent,options.toBundle());
        }
        else{
            startActivity(intent);
        }
    }

    public void

    public void callLoginScreen(View view){

        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);

    }
}