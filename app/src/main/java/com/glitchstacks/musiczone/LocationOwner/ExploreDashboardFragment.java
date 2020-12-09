package com.glitchstacks.musiczone.LocationOwner;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.glitchstacks.musiczone.Common.Login;
import com.glitchstacks.musiczone.Common.MusicZoneStartUpScreen;
import com.glitchstacks.musiczone.HelperClasses.ExplorePageAdapter.FeaturedAdapter;
import com.glitchstacks.musiczone.HelperClasses.ExplorePageAdapter.FeaturedHelperClass;
import com.glitchstacks.musiczone.HelperClasses.ExplorePageAdapter.MostViewedAdapter;
import com.glitchstacks.musiczone.HelperClasses.ExplorePageAdapter.MostViewedHelperClass;
import com.glitchstacks.musiczone.R;

import java.util.ArrayList;

public class ExploreDashboardFragment extends Fragment {

    RecyclerView featuredRecycler;
    RecyclerView.Adapter adapter1;

    RecyclerView mostViewedRecycler;
    RecyclerView.Adapter adapter2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_explore_dashboard, container, false);

        // Inflate the layout for this fragment
        featuredRecycler = root.findViewById(R.id.featured_recycler);
        featuredRecycler();

        mostViewedRecycler = root.findViewById(R.id.most_viewed_recycler);
        mostViewedRecycler();

        return root;
    }

    private void featuredRecycler() {

        featuredRecycler.setHasFixedSize(true);
        featuredRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        ArrayList<FeaturedHelperClass> featuredLocations = new ArrayList<>();

        featuredLocations.add(new FeaturedHelperClass(R.drawable.raissa_poster2,"Raissa Concert","Raissa concert has always been the best over the year"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.afgan_poster,"Afgan Concert","Afgan concert has always been the best over the year"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.isyana_poster,"Isyana Concert","Isyana concert has always been the best over the year"));

        adapter1 = new FeaturedAdapter(featuredLocations);
        featuredRecycler.setAdapter(adapter1);

    }

    private void mostViewedRecycler() {

        mostViewedRecycler.setHasFixedSize(true);
        mostViewedRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        ArrayList<MostViewedHelperClass> mostViewedLocations = new ArrayList<>();

        mostViewedLocations.add(new MostViewedHelperClass(R.drawable.raissa_poster2,"Raissa Concert","Raissa concert has always been the best over the year"));
        mostViewedLocations.add(new MostViewedHelperClass(R.drawable.afgan_poster,"Afgan Concert","Afgan concert has always been the best over the year"));
        mostViewedLocations.add(new MostViewedHelperClass(R.drawable.isyana_poster,"Isyana Concert","Isyana concert has always been the best over the year"));

        adapter2 = new MostViewedAdapter( mostViewedLocations);
        mostViewedRecycler.setAdapter(adapter2);

    }


}