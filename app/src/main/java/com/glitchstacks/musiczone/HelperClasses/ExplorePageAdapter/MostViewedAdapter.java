package com.glitchstacks.musiczone.HelperClasses.ExplorePageAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.glitchstacks.musiczone.R;

import java.util.ArrayList;

public class MostViewedAdapter  extends RecyclerView.Adapter<MostViewedAdapter.MostViewedHolder> {

    ArrayList<MostViewedHelperClass> mostViewedLocations;

    public MostViewedAdapter(ArrayList<MostViewedHelperClass> mostViewedLocations) {
        this.mostViewedLocations = mostViewedLocations;
    }

    @NonNull
    @Override
    public MostViewedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.most_viewed_card_design, parent,false);
        MostViewedAdapter.MostViewedHolder mostViewedHolder = new MostViewedAdapter.MostViewedHolder(view);
        return mostViewedHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MostViewedAdapter.MostViewedHolder holder, int position) {
        MostViewedHelperClass mostViewedHelperClass = mostViewedLocations.get(position);

        holder.image.setImageResource(mostViewedHelperClass.getImage());
        holder.title.setText(mostViewedHelperClass.getTitle());
        holder.passage .setText(mostViewedHelperClass.getPassage());
    }

    @Override
    public int getItemCount() {
        return mostViewedLocations.size();
    }

    public static class MostViewedHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title, passage;

        public MostViewedHolder(@NonNull View itemView) {
            super(itemView);

            // Hooks

            image = itemView.findViewById(R.id.most_viewed_image);
            title = itemView.findViewById(R.id.most_viewed_title);
            passage = itemView.findViewById(R.id.most_viewed_passage);


        }
    }

}
