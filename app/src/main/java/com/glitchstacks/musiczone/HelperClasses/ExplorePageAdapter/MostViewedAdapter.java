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

public class MostViewedAdapter  extends RecyclerView.Adapter<MostViewedAdapter.MostViewedHolder> {

    ArrayList<MostViewedHelperClass> mostViewedLocations;
    View view;

    public MostViewedAdapter(ArrayList<MostViewedHelperClass> mostViewedLocations) {
        this.mostViewedLocations = mostViewedLocations;
    }

    @NonNull
    @Override
    public MostViewedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.most_viewed_card_design, parent,false);

        MostViewedAdapter.MostViewedHolder mostViewedHolder = new MostViewedAdapter.MostViewedHolder(view);
        return mostViewedHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MostViewedAdapter.MostViewedHolder holder, int position) {
        MostViewedHelperClass mostViewedHelperClass = mostViewedLocations.get(position);

        String profileUrl = mostViewedHelperClass.getImage();

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

        holder.title.setText(mostViewedHelperClass.getTitle());
        holder.passage .setText(mostViewedHelperClass.getPassage());
        holder.date.setText(mostViewedHelperClass.getDate());


    }

    @Override
    public int getItemCount() {
        return mostViewedLocations.size();
    }

    public class MostViewedHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView image;
        public TextView title, passage,date;
        public MostViewedHolder(@NonNull View itemView) {
            super(itemView);

            // Hooks
            itemView.setOnClickListener(this);
            image = itemView.findViewById(R.id.most_viewed_image);
            title = itemView.findViewById(R.id.most_viewed_title);
            passage = itemView.findViewById(R.id.most_viewed_passage);
            date = itemView.findViewById(R.id.most_viewed_date);

        }

        @Override
        public void onClick(View view) {
            Log.d("clicked", "clicked");
            Intent intent = new Intent(view.getContext(), ConcertDetailActivity.class);

            // Position of the adapter
            Integer position = getAdapterPosition();

            String currentConcertKey = mostViewedLocations.get(position).getKey();

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
