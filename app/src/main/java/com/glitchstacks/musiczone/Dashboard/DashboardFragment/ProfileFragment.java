package com.glitchstacks.musiczone.Dashboard.DashboardFragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.glitchstacks.musiczone.Entries.MusicZoneStartUpScreen;
import com.glitchstacks.musiczone.Dashboard.RetailerDashboard;
import com.glitchstacks.musiczone.Profile.SettingsActivity;
import com.glitchstacks.musiczone.Database.SessionManager;
import com.glitchstacks.musiczone.PromotorPage;
import com.glitchstacks.musiczone.Profile.PromotorRequest;
import com.glitchstacks.musiczone.R;
import com.glitchstacks.musiczone.Profile.SavedConcerts;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    public RetailerDashboard activity;
    private ImageView mProfileImage;
    private RelativeLayout logout_layout;
    private RelativeLayout profile_layout, layout_saveConcert, layout_promotor;
    private TextView mNameField, mPhoneField, mEmailField, mDescField, txtFriends, txtSavedConcerts, txtConcerts;
    private DatabaseReference mUserDatabase;
    private SessionManager sessionManager;
    private ProgressDialog mProgress;
    private String phoneNumber, userId, name, phoneNo, email, desc, profileImageUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_profile, container, false);

        // Hook
        logout_layout = root.findViewById(R.id.logout_layout);
        profile_layout = root.findViewById(R.id.layout_profile);
        mNameField = root.findViewById(R.id.name);
        mPhoneField = root.findViewById(R.id.phone_number);
        mEmailField = root.findViewById(R.id.email);
        mDescField = root.findViewById(R.id.description_detail);
        mProfileImage = root.findViewById(R.id.profile_picture);
        txtConcerts = root.findViewById(R.id.txtConcerts);
        txtFriends = root.findViewById(R.id.txtFriends);
        txtSavedConcerts = root.findViewById(R.id.txtSavedConcerts);
        layout_saveConcert = root.findViewById(R.id.layout_savedConcert);
        layout_promotor = root.findViewById(R.id.layout_promotor);

        mProgress = new ProgressDialog(getActivity());


        // Current User
        sessionManager = new SessionManager(getContext(), SessionManager.SESSION_USERSESSION);
        HashMap<String, String> map = sessionManager.getUsersDetailFromSession();
        phoneNumber = map.get(SessionManager.KEY_PHONENUMBER);
        userId = phoneNumber;

        // User from Database
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        mProgress.setMessage("Fetching Data");
        mProgress.show();

        getUserInfo();

        mProgress.dismiss();

        layout_promotor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkPromotor();

            }
        });


        layout_saveConcert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SavedConcerts.class);
                startActivity(intent);
            }
        });

        logout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logoutUserSession();
                Intent intent = new Intent(getContext(), MusicZoneStartUpScreen.class);
                startActivity(intent);
                getActivity().finish();

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

    private void checkPromotor() {

        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    String accepted = snapshot.child("promotor").getValue().toString();

                    if(accepted.equals("false")){
                        checkRequest();
                    }else{
                        Intent intent = new Intent(getContext(), PromotorPage.class);
                        startActivity(intent);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkRequest() {

        DatabaseReference mRequest = FirebaseDatabase.getInstance().getReference().child("Request").child(userId);

        if(mRequest == null){
            Intent intent = new Intent(getContext(), PromotorRequest.class);
            startActivity(intent);
        }

        mRequest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String isAccepted = snapshot.child("accepted").getValue().toString();
                    if(isAccepted.equals("pending")){
                        Toast.makeText(getContext(),"Wait for the authentication", Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        Toast.makeText(getContext(), "You're not egligible to Apply", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                else{
                    Intent intent = new Intent(getContext(), PromotorRequest.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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
                    if (map.get("profileImageUrl") != null) {
                        profileImageUrl = map.get("profileImageUrl").toString();
                        switch (profileImageUrl) {
                            case "default":
                                Glide.with(getActivity().getApplication()).load(R.mipmap.ic_launcher).into(mProfileImage);
                                break;
                            default:
                                try {
                                    Glide.clear(mProfileImage);
                                    Glide.with(getActivity().getApplication()).load(profileImageUrl).into(mProfileImage);
                                }catch(Exception e){

                                }
                                break;
                        }
                    }

                    if(dataSnapshot.child("concertLikes").exists()){
                        Integer concertCount = Integer.parseInt(String.valueOf(dataSnapshot.child("concertLikes").getChildrenCount()));
                        txtSavedConcerts.setText(concertCount.toString());
                    }

                    if(dataSnapshot.child("connections").child("yeps").exists()){

                        Integer friendsCount = Integer.parseInt(String.valueOf(dataSnapshot.child("connections").child("matches").getChildrenCount()));
                        txtFriends.setText(String.valueOf(friendsCount));
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.activity = (RetailerDashboard) activity;
    }

}