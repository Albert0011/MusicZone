package com.glitchstacks.musiczone.Concert;

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
import com.glitchstacks.musiczone.Concert.ConcertDetailActivity;
import com.glitchstacks.musiczone.R;
import com.glitchstacks.musiczone.Track;
import com.glitchstacks.musiczone.TrackListener;

import java.util.ArrayList;
import java.util.List;

public class ConcertDetailTrackAdapter extends RecyclerView.Adapter<ConcertDetailTrackAdapter.ConcertDetailTrackViewHolder> {

    ArrayList<Track> trackArrayList;
    View view;

    public ConcertDetailTrackAdapter(ArrayList<Track> trackArrayList) {
        this.trackArrayList = trackArrayList;
    }

    @NonNull
    @Override
    public ConcertDetailTrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_adapter_blue, parent,false);
        ConcertDetailTrackViewHolder concertDetailTrackViewHolder = new ConcertDetailTrackViewHolder(view);
        return concertDetailTrackViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ConcertDetailTrackViewHolder holder, int position) {

        Log.d("track masuk","track masuk");

        Track track = trackArrayList.get(position);
        holder.bindTrack(track);
    }

    @Override
    public int getItemCount() {
        return trackArrayList.size();
    }

    public class ConcertDetailTrackViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        ImageView imageSelected;
        LinearLayout track_layout;

        public ConcertDetailTrackViewHolder(@NonNull View itemView) {
            super(itemView);
            // Hooks
            name = itemView.findViewById(R.id.track_name);
            imageSelected = itemView.findViewById(R.id.imageSelected);
            track_layout = itemView.findViewById(R.id.track_layout);
        }

        void bindTrack(final Track track){

            final String tracktitle = track.getName();

            name.setText(tracktitle);

            track_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("trackClicked", tracktitle);

                }
            });

            track_layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    Uri uri = Uri.parse(track.getSpotifyLink());
                    v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, uri));

                    return true;
                }
            });


        }

    }

}
