package com.glitchstacks.musiczone;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.glitchstacks.musiczone.Concert.ConcertDetailActivity;
import java.util.ArrayList;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> {

    ArrayList<Track> trackArrayList;
    View view;

    public TrackAdapter(ArrayList<Track> trackArrayList) {
        this.trackArrayList = trackArrayList;
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
        holder.name.setText(track.getName());
    }

    @Override
    public int getItemCount() {
        return trackArrayList.size();
    }

    public class TrackViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name;

        public TrackViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            // Hooks
            name = itemView.findViewById(R.id.track_name);
        }

        @Override
        public void onClick(View view) {
            Log.d("clicked", "clicked");
            Intent intent = new Intent(view.getContext(), ConcertDetailActivity.class);

            // Position of the adapter
            Integer position = getAdapterPosition();

        }
    }

}
