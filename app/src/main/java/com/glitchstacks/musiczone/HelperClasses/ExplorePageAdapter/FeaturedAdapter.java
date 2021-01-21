package com.glitchstacks.musiczone.HelperClasses.ExplorePageAdapter;

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
import com.glitchstacks.musiczone.Concert.ConcertDetailActivity;
import com.glitchstacks.musiczone.Database.SessionManager;
import com.glitchstacks.musiczone.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.FeaturedViewHolder> {

    ArrayList<FeaturedHelperClass> featuredLocations;
    View view;

    public FeaturedAdapter(ArrayList<FeaturedHelperClass> featuredLocations) {
        this.featuredLocations = featuredLocations;
    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_card_design, parent,false);
        FeaturedViewHolder featuredViewHolder = new FeaturedViewHolder(view);
        return featuredViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, int position) {
        FeaturedHelperClass featuredHelperClass = featuredLocations.get(position);

        String profileUrl = featuredHelperClass.getImage();

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

        Log.d("masuk set", "masuk set");

        holder.title.setText(featuredHelperClass.getTitle());
        holder.passage .setText(featuredHelperClass.getPassage());
        holder.date.setText(featuredHelperClass.getDate());

    }

    @Override
    public int getItemCount() {
        return featuredLocations.size();
    }

    public class FeaturedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image;
        TextView title, passage, date;

        public FeaturedViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            // Hooks
            image = itemView.findViewById(R.id.featured_image);
            title = itemView.findViewById(R.id.featured_title);
            passage = itemView.findViewById(R.id.featured_passage);
            date = itemView.findViewById(R.id.featured_date);


        }

        @Override
        public void onClick(View view) {
            Log.d("clicked", "clicked");
            Intent intent = new Intent(view.getContext(), ConcertDetailActivity.class);

            // Position of the adapter
            Integer position = getAdapterPosition();

            String currentConcertKey = featuredLocations.get(position).getKey();

            SessionManager sessionManager = new SessionManager(view.getContext(), SessionManager.SESSION_USERSESSION);
            HashMap<String, String> userDetails = sessionManager.getUsersDetailFromSession();

            final String phoneNumber = userDetails.get(SessionManager.KEY_PHONENUMBER);

            DatabaseReference mConcerts = FirebaseDatabase.getInstance().getReference().child("Concerts").child(currentConcertKey).child("concertView").child(phoneNumber);

            mConcerts.setValue("true");

            intent.putExtra("concertKey",currentConcertKey);

            view.getContext().startActivity(intent);

        }
    }

}
