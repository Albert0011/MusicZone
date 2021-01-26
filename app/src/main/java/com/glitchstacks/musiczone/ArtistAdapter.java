package com.glitchstacks.musiczone;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {

    ArrayList<Artist> artistArrayList;
    ArtistListener artistListener;
    View view;

    public ArtistAdapter(ArrayList<Artist> artistArrayList, ArtistListener artistListener) {
        this.artistArrayList = artistArrayList;
        this.artistListener = artistListener;
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

        holder.bindArtist(artist);

    }

    public List<Artist> getSelectedArtist(){
        List<Artist> selectedArtist = new ArrayList<>();

        for(Artist artist : artistArrayList){

            if(artist.isSelected){
                selectedArtist.add(artist);
            }

        }
        return selectedArtist;
    }


    @Override
    public int getItemCount() {
        return artistArrayList.size();
    }

    public class ArtistViewHolder extends RecyclerView.ViewHolder {

        LinearLayout artist_layout;
        ImageView image, imageSelected;
        TextView name;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);

            // Hooks
            image = itemView.findViewById(R.id.artist_image);
            name = itemView.findViewById(R.id.artist_name);
            imageSelected = itemView.findViewById(R.id.imageSelected);
            artist_layout = itemView.findViewById(R.id.artist_layout);

        }

        void bindArtist(final Artist artist){

            String profileUrl = artist.getImageURL();

            if(artist.isSelected){
                imageSelected.setVisibility(View.VISIBLE);
            }else{
                imageSelected.setVisibility(View.GONE);
            }

            if(!(profileUrl == null)){
                switch(profileUrl){
                    case "default":
                        Glide.with(view.getContext()).load(R.mipmap.ic_launcher).into(image);
                        break;
                    default:
                        Glide.clear(image);
                        Glide.with(view.getContext()).load(profileUrl).into(image);
                        break;
                }
            }
            name.setText(artist.getName());

            artist_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d("artistClickedBind", artist.getName());

                    if(artist.isSelected){
                        imageSelected.setVisibility(View.GONE);
                        artist.isSelected = false;

                        if(getSelectedArtist().size() == 0){
                            artistListener.onArtistAction(false);
                        }

                    }else{
                        imageSelected.setVisibility(View.VISIBLE);
                        artist.isSelected = true;
                        artistListener.onArtistAction(true);
                    }
                }
            });

            artist_layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    Uri uri = Uri.parse(artist.getSpotifyLink());
                    v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, uri));

                    return true;
                }
            });
        }
    }
}