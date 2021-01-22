package com.glitchstacks.musiczone.PromoteUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.glitchstacks.musiczone.Database.SessionManager;
import com.glitchstacks.musiczone.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PromoteActivity extends AppCompatActivity {

    private String id, name, dob,address,job;
    private EditText id_input, name_input, dob_input, address_input, job_input;
    private DatabaseReference mDatabase;
    private ImageView ktpImage;
    private Uri resultUri;
    private String areaPrice, areaName;
    private TextInputLayout id_layout, name_layout, dob_layout, address_layout, job_layout;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote);

        SessionManager sessionManager = new SessionManager(PromoteActivity.this, SessionManager.SESSION_USERSESSION);

        // User Information map
        HashMap<String, String> map = sessionManager.getUsersDetailFromSession();

        // UserID
        phoneNumber = map.get(SessionManager.KEY_PHONENUMBER);

        //Hooks
        Button nextBtn = (Button)findViewById(R.id.btnNext);

        id_input = findViewById(R.id.id_input);
        id_layout = findViewById(R.id.id_layout);

        name_input = findViewById(R.id.name_input);
        name_layout = findViewById(R.id.name_layout);

        dob_input = findViewById(R.id.dob_input);
        dob_layout = findViewById(R.id.dob_layout);

        address_input = findViewById(R.id.address_input);
        address_layout = findViewById(R.id.address_layout);

        job_input = findViewById(R.id.job_input);
        job_layout = findViewById(R.id.job_layout);

        ktpImage = findViewById(R.id.imgArea);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callNextScreen();
            }
        });

        ktpImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

    }

    private void saveKtpInformation() {

        Boolean a, b, c, d, e;
        a = isIdValid();
        b = isNameValid();
        c = isAddressValid();
        d = isDobValid();
        e = isJobValid();

        if(!a || !b || !c || !d || !e){
            return;
        }

        String key = getIntent().getStringExtra("key");

        id = id_input.getText().toString();
        name = name_input.getText().toString();
        dob = dob_input.getText().toString();
        address = address_input.getText().toString();
        job = job_input.getText().toString();

        DatabaseReference ktpDatabase = mDatabase.child("Users").child(phoneNumber).child("PromoteDetail");

        final Map ktpInfo = new HashMap();
        ktpInfo.put("id", id);
        ktpInfo.put("name", name);
        ktpInfo.put("dob",dob);
        ktpInfo.put("address",address);
        ktpInfo.put("job",job);

        // Database Reference
        ktpDatabase.push().updateChildren(ktpInfo);

    }

    private Boolean isPictureValid(){
        if(resultUri == null){
            Toast.makeText(getApplicationContext(), "Picture must be chosen!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private Boolean isDobValid() {

        dob = dob_input.getText().toString();
        if(dob.isEmpty()){
            dob_layout.setError("field can't be empty");
            return false;
        } else{
            dob_layout.setError(null);
            dob_layout.setErrorEnabled(false);
        }

        return true;
    }

    private Boolean isAddressValid() {

        address = address_input.getText().toString();
        if(address.isEmpty()){
            address_layout.setError("field can't be empty");
            return false;
        } else{
            address_layout.setError(null);
            address_layout.setErrorEnabled(false);
        }

        return true;
    }

    private Boolean isJobValid() {

        job = job_input.getText().toString();
        if(job.isEmpty()){
            job_layout.setError("field can't be empty");
            return false;
        } else{
            job_layout.setError(null);
            job_layout.setErrorEnabled(false);
        }

        return true;
    }

    private Boolean isIdValid() {

        id = id_input.getText().toString();
        if(id.isEmpty()){
            id_layout.setError("field can't be empty");
            return false;
        } else{
            id_layout.setError(null);
            id_layout.setErrorEnabled(false);
        }

        return true;
    }

    private Boolean isNameValid() {

        name = name_input.getText().toString();

        if(name.isEmpty()){
            name_layout.setError("field can't be empty");
            return false;
        } else {
            name_layout.setError(null);
            name_layout.setErrorEnabled(false);
        }

        return true;
    }

    public void saveImage(){


        final String key = getIntent().getStringExtra("key");

        final DatabaseReference areaDatabase = mDatabase.child("Areas").child(key);

        if(resultUri != null){
            final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("areaImages").child(key);

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getApplication().getContentResolver(), resultUri);
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


                            Map areaInfo = new HashMap();
                            areaInfo.put("imageURL", downloadUrl.toString());
                            areaDatabase.child("area_image").updateChildren(areaInfo);

                            //getActivity().finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
                    /*
                    Task<Uri> downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                     */
                }
            });
        }

    }

    public void callNextScreen() {

        saveKtpInformation();

        if(!isPictureValid()){
            return;
        }

        saveImage();

        String key = getIntent().getStringExtra("key");

        Intent intent = new Intent(getApplicationContext(), WaitEmail.class);
        intent.putExtra("key",key);

        startActivity(intent);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            ktpImage.setImageURI(resultUri);
        } else {
            Toast.makeText(this, "in", Toast.LENGTH_LONG).show();
        }
    }


}