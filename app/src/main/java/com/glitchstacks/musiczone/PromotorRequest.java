package com.glitchstacks.musiczone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.glitchstacks.musiczone.Dashboard.RetailerDashboard;
import com.glitchstacks.musiczone.Database.SessionManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PromotorRequest extends AppCompatActivity {

    private ImageView imageIdentity;
    private TextInputEditText inputAccountNumber;
    private TextView textReadTerm;
    private CheckBox checkTerm;
    private Button buttonSubmit;
    private Dialog dialog;
    private TextInputLayout layoutBank;
    private Uri resultUri;
    private String phoneNumber;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotor_request);

        imageIdentity = findViewById(R.id.imageIdentity);
        inputAccountNumber = findViewById(R.id.accountNumber);
        textReadTerm = findViewById(R.id.textReadTerm);
        checkTerm = findViewById(R.id.checkbox_agree);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        layoutBank = findViewById(R.id.layoutAccountNumber);
        backButton = findViewById(R.id.back_btn);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textReadTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTerm();
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateForm();
            }
        });

        imageIdentity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        SessionManager sessionManager = new SessionManager(PromotorRequest.this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> map = sessionManager.getUsersDetailFromSession();
        phoneNumber = map.get(SessionManager.KEY_PHONENUMBER);

    }

    private void ShowTerm() {

        // Initiaize dialog
        dialog = new Dialog(PromotorRequest.this);

        // Set customer Dialog
        dialog.setContentView(R.layout.term_and_conditions);
        dialog.getWindow().setLayout(1000, 1500);
        // transparent
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.show();

    }

    private void ValidateForm() {

        Boolean a,b;

        a = isBankValid();
        b = isAgree();

        if(!a || !b || (resultUri == null)){
            return;
        }

        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("promotor").child(phoneNumber);
        Bitmap bitmap = null;

        try {
            bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = filepath.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

//                    getActivity().finish();
            }
        });

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUrl) {
                        Map userInfo = new HashMap();
                        userInfo.put("bank_acc", inputAccountNumber.getText().toString());
                        userInfo.put("identity_url", downloadUrl.toString());
                        userInfo.put("accepted", "pending");

                        mDatabase.child("Request").child(phoneNumber).updateChildren(userInfo);
                        //getActivity().finish();

                    }
                });
            }
        });

        Intent intent = new Intent(PromotorRequest.this, RetailerDashboard.class);
        startActivity(intent);
        finish();

    }

    private boolean isBankValid(){

        String bankAccount = inputAccountNumber.getText().toString();

        if(bankAccount.isEmpty()){
            layoutBank.setErrorEnabled(true);
            layoutBank.setError("Field cannot be empty!");

            return false;
        }
        layoutBank.setErrorEnabled(false);
        return true;
    }

    private boolean isAgree(){
        if(checkTerm.isChecked()){
            return true;
        }
        Toast.makeText(PromotorRequest.this, "You must be agree the term and conditions", Toast.LENGTH_SHORT).show();
        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("FragmentSettingActivity.java", "onActivityResult called");
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            imageIdentity.setImageURI(resultUri);
        } else {
            Log.d("pictureChanged", "cancelled");
        }
    }


}