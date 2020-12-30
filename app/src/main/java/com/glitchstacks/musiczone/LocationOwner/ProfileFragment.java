package com.glitchstacks.musiczone.LocationOwner;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.glitchstacks.musiczone.Common.IntroductoryActivity;
import com.glitchstacks.musiczone.Common.MusicZoneStartUpScreen;
import com.glitchstacks.musiczone.Common.RetailerDashboard;
import com.glitchstacks.musiczone.Database.SessionManager;
import com.glitchstacks.musiczone.Entries.Login;
import com.glitchstacks.musiczone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class ProfileFragment extends Fragment {

    RelativeLayout logout_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_profile, container, false);

        logout_layout = root.findViewById(R.id.logout_layout);

        final SessionManager sessionManager = new SessionManager(getContext(), SessionManager.SESSION_USERSESSION);


        logout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sessionManager.logoutUserSession();
                Intent intent = new Intent(getContext(), MusicZoneStartUpScreen.class);
                startActivity(intent);

            }
        });

        return root;
    }
}