package com.glitchstacks.musiczone.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.glitchstacks.musiczone.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetNewPassword extends AppCompatActivity {

    EditText confirmPasswordInput, newPasswordInput;
    TextInputLayout confirmPasswordLayout, newPasswordLayout;
    Button okBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        newPasswordInput = findViewById(R.id.newPasswordInput);
        newPasswordLayout = findViewById(R.id.newPasswordLayout);
        confirmPasswordLayout = findViewById(R.id.confirmPasswordLayout);

        okBtn = findViewById(R.id.okBtn);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });

    }

    public void updatePassword(){
        if(!validate()){
            return;
        }

        String _newPassword = newPasswordInput.getText().toString();
        String _phoneNumber = getIntent().getStringExtra("phoneNo");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(_phoneNumber).child("password").setValue(_newPassword);

        startActivity(new Intent(getApplicationContext(), ForgetPasswordSuccessMessage.class));
        finish();

    }

    public boolean validate(){

        boolean a = validatePassword();
        boolean b = checkPassword();

        if(!a || !b){
            return false;
        } else {
            return true;
        }

    }

    private boolean checkPassword(){
        String newPassword = newPasswordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();

        if(newPassword.equals(confirmPassword)){
            confirmPasswordLayout.setError(null);
            confirmPasswordLayout.setErrorEnabled(false);
            return true;
        } else {
            confirmPasswordLayout.setError("Password does not match");
            confirmPasswordLayout.requestFocus();
            return false;
        }
    }


    public static boolean isValidPassword(String password,String regex)
    {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private boolean validatePassword() {

        String newPassword = newPasswordInput.getText().toString();

        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{4,20}$";

        if (newPassword.isEmpty()) {
            newPasswordLayout.setError("Field can not be empty");
            return false;
        } else if (!isValidPassword(newPassword, regex)) {
            newPasswordLayout.setError("Password is too weak!");
            return false;
        } else {
            newPasswordLayout.setError(null);
            newPasswordLayout.setErrorEnabled(false);
            return true;
        }
    }

}