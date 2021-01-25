package com.glitchstacks.musiczone.Concert;

import android.content.Intent;
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
import com.glitchstacks.musiczone.ArtistListener;
import com.glitchstacks.musiczone.Concert.ConcertDetailActivity;
import com.glitchstacks.musiczone.Database.SessionManager;
import com.glitchstacks.musiczone.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConcertDetailArtistAdapter extends RecyclerView.Adapter<ConcertDetailArtistAdapter.ConcertDetailArtistViewHolder> {

    ArrayList<Artist> artistArrayList;
    View view;

    public ConcertDetailArtistAdapter(ArrayList<Artist> artistArrayList) {
        this.artistArrayList = artistArrayList;
    }

    @NonNull
    @Override
    public ConcertDetailArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_artist_adapter_black, parent,false);
        ConcertDetailArtistViewHolder concertDetailArtistViewHolder = new ConcertDetailArtistViewHolder(view);
        return concertDetailArtistViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ConcertDetailArtistViewHolder holder, int position) {
        Artist artist = artistArrayList.get(position);
        holder.bindArtist(artist);

    }

    @Override
    public int getItemCount() {
        return artistArrayList.size();
    }

    public class ConcertDetailArtistViewHolder extends RecyclerView.ViewHolder {

        LinearLayout artist_layout;
        ImageView image, imageSelected;
        TextView name;

        public ConcertDetailArtistViewHolder(@NonNull View itemView) {
            super(itemView);

            // Hooks
            image = itemView.findViewById(R.id.artist_image);
            name = itemView.findViewById(R.id.artist_name);
            imageSelected = itemView.findViewById(R.id.imageSelected);
            artist_layout = itemView.findViewById(R.id.artist_layout);

        }

        void bindArtist(final Artist artist){

            String profileUrl = artist.getImageURL();

            if(!profileUrl.isEmpty()){
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

                    String concertKey = ConcertDetailActivity.getConcertKey();

                    Intent intent = new Intent(artist_layout.getContext(), DetailPlaylist.class);
                    intent.putExtra("artistID", artist.getId());
                    intent.putExtra("concertKey", concertKey);
                    artist_layout.getContext().startActivity(intent);

                }
            });
        }
    }
}