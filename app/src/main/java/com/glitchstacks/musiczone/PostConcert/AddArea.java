package com.glitchstacks.musiczone.PostConcert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddArea extends AppCompatActivity {

    private EditText nameInput, priceInput;
    private DatabaseReference mDatabase;
    private ImageView areaImage;
    private Uri resultUri;
    private String areaPrice, areaName;
    private TextInputLayout priceLayout, nameLayout;
    private ArrayList<Area> areaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_area);

        SessionManager sessionManager = new SessionManager(AddArea.this, SessionManager.SESSION_USERSESSION);

        // User Information map
        HashMap<String, String> map = sessionManager.getUsersDetailFromSession();

        // UserID
        String phoneNumber = map.get(SessionManager.KEY_PHONENUMBER);

        //Hooks
        Button nextBtn = (Button)findViewById(R.id.btnNext);
        Button add_area = (Button)findViewById(R.id.btnAddArea);
        areaList = new ArrayList<>();

        nameLayout = findViewById(R.id.name_layout);
        priceLayout = findViewById(R.id.price_layout);

        nameInput = findViewById(R.id.name_input);
        priceInput = findViewById(R.id.price_input);

        areaImage = findViewById(R.id.imgArea);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        add_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAreaInformation();
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callNextScreen();
            }
        });

        areaImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

    }

    private void saveAreaInformation() {

        Boolean a, b;
        a = isPriceValid();
        b = isNameValid();

        if(!a || !b){
            return;
        }

        Area currentArea = new Area(areaPrice, areaName);

        for (Area ar : areaList) {
            if(ar.getAreaName().equals(areaName)){
                Toast.makeText(this, "Area is already in the list!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        areaList.add(currentArea);
        Toast.makeText(this, areaName + " with the price of" + areaPrice + " is successfully added!", Toast.LENGTH_SHORT).show();

    }

    private Boolean isPictureValid(){
        if(resultUri == null){
            Toast.makeText(getApplicationContext(), "Picture must be chosen!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private Boolean isNameValid() {

        areaName = nameInput.getText().toString();
        if(areaName.isEmpty()){
            nameLayout.setError("field can't be empty");
            return false;
        } else {
            nameLayout.setError(null);
            nameLayout.setErrorEnabled(false);
        }

        return true;
    }

    private Boolean isPriceValid() {

        areaPrice = priceInput.getText().toString();

        if(areaPrice.isEmpty()){
            priceLayout.setError("field can't be empty");
            return false;
        }

        Integer areaPriceInt = Integer.parseInt(areaPrice);

        if(areaPriceInt < 0){
            priceLayout.setError("price cannot go lower than 0");
            return false;
        }
        else{
            if(areaPriceInt > 20000000){
                priceLayout.setError("price is too expensive!");
                return false;
            } else{
                priceLayout.setError(null);
                priceLayout.setErrorEnabled(false);
            }
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

        if(!isPictureValid()){
            Toast.makeText(AddArea.this, "Choose area picture first!", Toast.LENGTH_SHORT).show();
            return;
        }

        //saveImage();
        Intent intent = new Intent(getApplicationContext(), AddAddress.class);

        String concertName = getIntent().getStringExtra("concertName");
        String concertKey = getIntent().getStringExtra("concertKey");
        String concertMainGenre = getIntent().getStringExtra("concertMainGenre");
        String concertDescription = getIntent().getStringExtra("concertDescription");
        String concertDuration = getIntent().getStringExtra("concertDuration");
        String concertDate = getIntent().getStringExtra("concertDate");
        String concertTime = getIntent().getStringExtra("concertTime");
        Uri concertImageUri = getIntent().getParcelableExtra("concertimageUri");
        ArrayList<Song> concertList = (ArrayList<Song>) getIntent().getSerializableExtra("playlist");

        Log.d("IniAddArea", "something is missing" + concertName + concertKey + concertMainGenre + concertDescription + concertDuration + concertDate + concertTime + (concertImageUri == null));


        if(concertName.isEmpty() || concertKey.isEmpty() || concertMainGenre.isEmpty() || concertDate.isEmpty() || concertDuration.isEmpty() || concertImageUri == null || concertList.size() == 0 || concertTime.isEmpty() ){
            Log.d("AddArea", "something is missing" + concertName + concertKey + concertMainGenre + concertDescription + concertDuration + concertDate + concertTime + (concertImageUri == null));
            return;
        }

        intent.putExtra("concertName",concertName);
        intent.putExtra("concertKey",concertKey);
        intent.putExtra("concertMainGenre",concertMainGenre);
        intent.putExtra("concertDescription",concertDescription);
        intent.putExtra("concertDuration",concertDuration);
        intent.putExtra("concertDate",concertDate);
        intent.putExtra("concertTime",concertTime);
        intent.putExtra("concertimageUri",concertImageUri);
        intent.putExtra("playlist",concertList);
        intent.putExtra("areaList",areaList);
        intent.putExtra("areaUri", resultUri);

        startActivity(intent);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            areaImage.setImageURI(resultUri);
        } else {
            Toast.makeText(this, "in", Toast.LENGTH_LONG).show();
        }
    }


}