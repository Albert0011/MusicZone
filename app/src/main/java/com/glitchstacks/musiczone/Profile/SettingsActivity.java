package com.glitchstacks.musiczone.Profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.glitchstacks.musiczone.Database.SessionManager;
import com.glitchstacks.musiczone.Dashboard.DashboardFragment.ProfileFragment;
import com.glitchstacks.musiczone.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class SettingsActivity extends Fragment {

    private EditText mNameField, mPhoneField, mEmailField, mDescField;
    private Button mBack, mConfirm;
    private ImageView mProfileImage;
    private DatabaseReference mUserDatabase;
    private String userId, name, phone, profileImageUrl, userSex, phoneNumber, email, desc;
    private Uri resultUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_settings, container, false);

        // Hook
        mNameField = (EditText) root.findViewById(R.id.name);
        mEmailField = (EditText) root.findViewById(R.id.email);
        mDescField = (EditText) root.findViewById(R.id.desc);
        mProfileImage = (ImageView) root.findViewById(R.id.profileImage);
        mBack = (Button) root.findViewById(R.id.back);
        mConfirm = (Button) root.findViewById(R.id.confirm);

        // Current User
        SessionManager sessionManager = new SessionManager(getContext(), SessionManager.SESSION_USERSESSION);
        HashMap<String, String> map = sessionManager.getUsersDetailFromSession();
        phoneNumber = map.get(SessionManager.KEY_PHONENUMBER);
        userId = phoneNumber;

        // User from Database
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        // getting User info
        getUserInfo();

        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                return;
            }
        });

        return root;

    }


    private void getUserInfo() {
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("fullname") != null) {
                        name = map.get("fullname").toString();
                        mNameField.setText(name);
                    }
                    if (map.get("gender") != null) {
                        userSex = map.get("gender").toString();
                    }
                    if (map.get("email") != null) {
                        email = map.get("email").toString();
                        mEmailField.setText(email);
                    }
                    if (map.get("description") != null) {
                        desc = map.get("description").toString();
                        mDescField.setText(desc);
                    }

                    Glide.clear(mProfileImage);
                    if (map.get("profileImageUrl") != null) {
                        profileImageUrl = map.get("profileImageUrl").toString();
                        switch (profileImageUrl) {
                            case "default":
                                Glide.with(getActivity().getApplication()).load(R.mipmap.ic_launcher).into(mProfileImage);
                                break;
                            default:
                                Glide.with(getActivity().getApplication()).load(profileImageUrl).into(mProfileImage);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void saveUserInformation() {
        name = mNameField.getText().toString();
        email = mEmailField.getText().toString();
        desc = mDescField.getText().toString();

        // HashMap
        Map userInfo = new HashMap();
        userInfo.put("fullname", name);
        userInfo.put("email",email);
        userInfo.put("description",desc);

        // Update Database Information
        mUserDatabase.updateChildren(userInfo);

        if (resultUri != null) {
            final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("profileImages").child(userId);
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplication().getContentResolver(), resultUri);
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
                            userInfo.put("profileImageUrl", downloadUrl.toString());
                            mUserDatabase.updateChildren(userInfo);

                            //getActivity().finish();

                        }
                    });
                    /*
                    Task<Uri> downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                     */
                }
            });
        } else {

            //getActivity().finish();

        }

        Fragment fragment = new ProfileFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("FragmentSettingActivity.java","onActivityResult called");
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            mProfileImage.setImageURI(resultUri);
        }
        else{
            Log.d("profileChanged", "cancelled");
        }
    }
}