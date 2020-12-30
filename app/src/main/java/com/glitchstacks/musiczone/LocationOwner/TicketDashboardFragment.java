package com.glitchstacks.musiczone.LocationOwner;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.glitchstacks.musiczone.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;


public class TicketDashboardFragment extends Fragment {

    ChipNavigationBar chipNavigationBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_ticket_dashboard, container, false);

        return root;
    }

}