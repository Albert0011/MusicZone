package com.glitchstacks.musiczone.Common;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.glitchstacks.musiczone.R;

public class OnBoardingFragment3 extends Fragment{

    Button fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.welcome_page3, container, false);

        fab = root.findViewById(R.id.btn_getstarted);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MusicZoneStartUpScreen.class);
                startActivity(intent);
            }
        });


        return root;
    }
}
