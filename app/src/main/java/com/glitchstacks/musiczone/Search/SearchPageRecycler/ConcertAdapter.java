package com.glitchstacks.musiczone.Search.SearchPageRecycler;

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

public class ConcertAdapter extends RecyclerView.Adapter<ConcertAdapter.ConcertAdapterViewHolder> {

    ArrayList<ConcertObject> concertObjectLocations;
    View view;

    public ConcertAdapter(ArrayList<ConcertObject> concertObjectLocations) {
        this.concertObjectLocations = concertObjectLocations;
    }

    @NonNull
    @Override
    public ConcertAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_concert_card, parent,false);
        ConcertAdapter.ConcertAdapterViewHolder concertAdapterViewHolder = new ConcertAdapter.ConcertAdapterViewHolder(view);
        return concertAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ConcertAdapter.ConcertAdapterViewHolder holder, int position) {
        ConcertObject ConcertObject = concertObjectLocations.get(position);

        String profileUrl = ConcertObject.getImage();

        if(holder == null){
            Log.d("viewisnull", "viewisnull");
        }

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

        holder.title.setText(ConcertObject.getTitle());
        holder.passage .setText(ConcertObject.getPassage());
        holder.date.setText(ConcertObject.getDate());


    }

    @Override
    public int getItemCount() {
        return concertObjectLocations.size();
    }

    public class ConcertAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView image;
        public TextView title, passage,date;
        public ConcertAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            // Hooks
            itemView.setOnClickListener(this);
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

            String currentConcertKey = concertObjectLocations.get(position).getKey();

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
