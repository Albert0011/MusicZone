package com.glitchstacks.musiczone;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.glitchstacks.musiczone.Artist;
import com.glitchstacks.musiczone.Concert.ConcertDetailActivity;
import com.glitchstacks.musiczone.Database.SessionManager;
import com.glitchstacks.musiczone.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {

    ArrayList<Artist> artistArrayList;
    View view;

    public ArtistAdapter(ArrayList<Artist> artistArrayList) {
        this.artistArrayList = artistArrayList;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_artist_adapter, parent,false);
        ArtistViewHolder artistViewHolder = new ArtistViewHolder(view);
        return artistViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        Artist artist = artistArrayList.get(position);

        String profileUrl = artist.getImageURL();

        if(!profileUrl.isEmpty()){
            switch(profileUrl){
                case "default":
                    Glide.with(view.getContext()).load(R.mipmap.ic_launcher).into(holder.image);
                    break;
                default:
                    Glide.clear(holder.image);
                    Glide.with(view.getContext()).load(profileUrl).into(holder.image);
                    break;
            }
        }

        holder.name.setText(artist.getName());

    }

    @Override
    public int getItemCount() {
        return artistArrayList.size();
    }

    public class ArtistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image;
        TextView name;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            // Hooks
            image = itemView.findViewById(R.id.artist_image);
            name = itemView.findViewById(R.id.artist_name);


        }

        @Override
        public void onClick(View view) {
            Log.d("clicked", "clicked");
            Intent intent = new Intent(view.getContext(), ConcertDetailActivity.class);

            // Position of the adapter
            Integer position = getAdapterPosition();

            // String currentConcertKey = artistArrayList.get(position).get;


        }
    }

}
