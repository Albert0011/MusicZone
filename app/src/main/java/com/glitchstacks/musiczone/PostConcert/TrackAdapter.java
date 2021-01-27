package com.glitchstacks.musiczone.PostConcert;

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

import com.glitchstacks.musiczone.R;

import java.util.ArrayList;
import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> {

    TrackListener trackListener;
    ArrayList<Track> trackArrayList;
    View view;

    public TrackAdapter(ArrayList<Track> trackArrayList, TrackListener trackListener) {
        this.trackArrayList = trackArrayList;
        this.trackListener = trackListener;
    }

    @NonNull
    @Override
    public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_track_adapter, parent,false);
        TrackViewHolder trackViewHolder = new TrackViewHolder(view);
        return trackViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder holder, int position) {

        Log.d("track masuk","track masuk");

        Track track = trackArrayList.get(position);
        holder.bindTrack(track);
    }

    public List<Track> getSelectedTrack(){
        List<Track> selectedTrack = new ArrayList<>();

        for(Track track : trackArrayList){
            if(track.isSelected){
                selectedTrack.add(track);
            }
        }

        return selectedTrack;

    }

    @Override
    public int getItemCount() {
        return trackArrayList.size();
    }

    public class TrackViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        ImageView imageSelected;
        LinearLayout track_layout;

        public TrackViewHolder(@NonNull View itemView) {
            super(itemView);
            // Hooks
            name = itemView.findViewById(R.id.track_name);
            imageSelected = itemView.findViewById(R.id.imageSelected);
            track_layout = itemView.findViewById(R.id.track_layout);
        }

        void bindTrack(final Track track){

            final String tracktitle = track.getName();

            if(track.isSelected){
                imageSelected.setVisibility(View.VISIBLE);
            }
            else{
                imageSelected.setVisibility(View.GONE);
            }

            name.setText(tracktitle);

            track_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("trackClicked", tracktitle);

                    if(track.isSelected) {
                        imageSelected.setVisibility(View.GONE);
                        track.isSelected = false;

                        if(getSelectedTrack().size() == 0){
                            trackListener.onTrackAction(false);
                        }


                    }else{
                        imageSelected.setVisibility(View.VISIBLE);
                        track.isSelected = true;
                        trackListener.onTrackAction(true);
                    }

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
