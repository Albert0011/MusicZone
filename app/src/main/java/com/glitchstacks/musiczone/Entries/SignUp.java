package com.glitchstacks.musiczone.Entries;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.glitchstacks.musiczone.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    //Variables
    ImageView backBtn;
    Button next, signin;
    TextView titletext;

    TextInputLayout fullname, username, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_signup);

        //Hooks
        backBtn = findViewById(R.id.signup_back_btn);
        next = findViewById(R.id.signup_next_btn);
        signin = findViewById(R.id.signup_signin_btn);
        titletext = findViewById(R.id.signup_title_text);

        fullname = findViewById(R.id.fullname_input);
        email = findViewById(R.id.email_input);
        username = findViewById(R.id.username_input);
        password = findViewById(R.id.password_input);

    }

    public void callNextSignUpScreen(View view) {

        Boolean a,b,c,d;
        a = validateEmail();
        b = validateFullname();
        c = validateUsername();
        d = validatePassword();


        if(!a || !b || !c || !d){
            return;
        }

        String _fullname = fullname.getEditText().getText().toString();
        String _email = email.getEditText().getText().toString();
        String _username = username.getEditText().getText().toString();
        String _password = password.getEditText().getText().toString();

        Intent intent = new Intent(getApplicationContext(), SignUp2ndClass.class);


        intent.putExtra("fullname", _fullname);
        intent.putExtra("email", _email);
        intent.putExtra("username", _username);
        intent.putExtra("password", _password);

        //Add Transition
        Pair[] pairs = new Pair[4];

        pairs[0] = new Pair<View, String>(backBtn, "transition_back_btn");
        pairs[1] = new Pair<View, String>(titletext, "transition_title_text");
        pairs[2] = new Pair<View, String>(next, "transition_next_btn");
        pairs[3] = new Pair<View, String>(signin, "transition_signin_btn");

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    //Validation
    private boolean validateFullname() {
        String val = fullname.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            fullname.setError("Field cannot be empty");
            return false;
        } else {
            fullname.setError(null);
            fullname.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validateUsername() {
        String val = username.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,20}\\z";

        if (val.isEmpty()) {
            username.setError("Field cannot be empty");
            return false;
        } else if (val.length() > 20) {
            username.setError("Username is too long!");
            return false;
        } else if (!val.matches(checkspaces)) {
            username.setError("White space is now allowed!");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validateEmail() {
        String val = email.getEditText().getText().toString().trim();
        String checkemail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            email.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(checkemail)) {
            email.setError("Invalid Email Address");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }

    }

    public static boolean isValidPassword(String password,String regex)
    {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }


    private boolean validatePassword() {

        String val = password.getEditText().getText().toString().trim();

        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{4,20}$";

        if (val.isEmpty()) {
            password.setError("Field can not be empty");
            return false;
        } else if (!isValidPassword(val, regex)) {
            password.setError("Password is too weak!");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }

    }


    public void backToBefore(View view) {

        Intent intent = new Intent(getApplicationContext(), MusicZoneStartUpScreen.class);

        Pair[] pairs = new Pair[1];

        pairs[0] = new Pair<View, String>(findViewById(R.id.signup_back_btn), "transition_back_btn");

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    public void callLoginScreen(View view) {

        Intent intent = new Intent(getApplicationContext(), Login.class);

        startActivity(intent);

    }
}