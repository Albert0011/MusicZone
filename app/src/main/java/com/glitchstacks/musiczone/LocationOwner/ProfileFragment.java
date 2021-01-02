package com.glitchstacks.musiczone.LocationOwner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.glitchstacks.musiczone.Common.IntroductoryActivity;
import com.glitchstacks.musiczone.Common.MusicZoneStartUpScreen;
import com.glitchstacks.musiczone.Common.RetailerDashboard;
import com.glitchstacks.musiczone.Common.SettingsActivity;
import com.glitchstacks.musiczone.Database.SessionManager;
import com.glitchstacks.musiczone.Entries.Login;
import com.glitchstacks.musiczone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    ImageView mProfileImage;
    RelativeLayout logout_layout;
    RelativeLayout profile_layout;
    TextView mNameField, mPhoneField, mEmailField, mDescField;
    DatabaseReference mUserDatabase;
    SessionManager sessionManager;
    ProgressDialog mProgress;
    String phoneNumber, userId, name, phoneNo, email, desc, profileImageUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_profile, container, false);

        // Hook
        logout_layout = root.findViewById(R.id.logout_layout);
        profile_layout = root.findViewById(R.id.layout_profile);
        mNameField = root.findViewById(R.id.name);
        mPhoneField = root.findViewById(R.id.phone_number);
        mEmailField = root.findViewById(R.id.email);
        mDescField = root.findViewById(R.id.description_detail);
        mProfileImage = root.findViewById(R.id.profile_picture);
        mProgress = new ProgressDialog(getActivity());

        // Current User
        sessionManager = new SessionManager(getContext(), SessionManager.SESSION_USERSESSION);
        HashMap<String, String> map = sessionManager.getUsersDetailFromSession();
        phoneNumber = map.get(SessionManager.KEY_PHONENUMBER);
        userId = phoneNumber;

        Toast.makeText(getContext(), map.get(SessionManager.KEY_SESSIONPASSWORD), Toast.LENGTH_SHORT).show();
        // User from Database
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        mProgress.setMessage("Fetching Data");
        mProgress.show();

        getUserInfo();

        mProgress.dismiss();

        logout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sessionManager.logoutUserSession();
                Intent intent = new Intent(getContext(), MusicZoneStartUpScreen.class);
                startActivity(intent);

            }
        });

        profile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsActivity()).commit();
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
                    if (map.get("phoneNo") != null) {
                        phoneNo = map.get("phoneNo").toString();
                        mPhoneField.setText(phoneNo);
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

}