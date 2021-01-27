package com.glitchstacks.musiczone;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ConcertAdapter extends RecyclerView.Adapter<ConcertAdapter.ConcertViewHolder>{

    ArrayList<PromotorConcert> concertList;
    PromotorConcertListener concertListener;
    View view;

    public ConcertAdapter(ArrayList<PromotorConcert> concertList, PromotorConcertListener concertListener) {
        this.concertList = concertList;
        this.concertListener = concertListener;
    }

    @NonNull
    @Override
    public ConcertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        this.view = LayoutInflater.from(parent.getContext()).inflate(R.layout.concert_promotor_card, parent,false);
        ConcertViewHolder concertViewHolder = new ConcertViewHolder(view);
        return concertViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ConcertViewHolder holder, int position) {
        PromotorConcert concert = concertList.get(position);
        holder.bindConcert(concert);
    }


    public ArrayList<PromotorConcert> getSelectedConcert(){

        ArrayList<PromotorConcert> selectedConcert =  new ArrayList<>();

        for(PromotorConcert concert : concertList){
            if(concert.isSelected){
                selectedConcert.add(concert);
            }
        }

        return selectedConcert;
    }

    public void printOutIn(){
        return;
    }


    @Override
    public int getItemCount() {
        return concertList.size();
    }

    public class ConcertViewHolder extends RecyclerView.ViewHolder{

        private RelativeLayout concert_layout;
        private TextView concert_name, concert_desc, concert_date;
        private ImageView concert_image, imageSelected;

        public ConcertViewHolder(@NonNull View itemView) {
            super(itemView);

            concert_layout = itemView.findViewById(R.id.concert_layout);
            concert_name = itemView.findViewById(R.id.concert_title);
            concert_date = itemView.findViewById(R.id.concert_date);
            concert_desc = itemView.findViewById(R.id.concert_desc);
            concert_image = itemView.findViewById(R.id.concert_image);
            imageSelected = itemView.findViewById(R.id.imageSelected);
        }

        public void bindConcert(final PromotorConcert concert) {

            if(concert.isSelected){
                imageSelected.setVisibility(View.VISIBLE);
            }else{
                imageSelected.setVisibility(View.GONE);
            }

            final String concertName = concert.getConcertName();
            final String concertDesc = concert.getConcertDescription();
            final String concertDate = concert.getConcertDate();
            final String concertImageURL = concert.getConcertImageURL();
            final String concertKey = concert.getConcertKey();

            concert_name.setText(concertName);
            concert_desc.setText(concertDesc);
            concert_date.setText(concertDate);

            if(concertImageURL != null){
                switch(concertImageURL){
                    case "default":
                        Glide.with(view.getContext()).load(R.mipmap.ic_launcher).into(concert_image);
                        break;
                    default:
                        Glide.clear(concert_image);
                        Glide.with(view.getContext()).load(concertImageURL).into(concert_image);
                        break;
                }
            }

            concert_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(concert.isSelected){
                        imageSelected.setVisibility(View.GONE);
                        concert.isSelected = false;

                        if(getSelectedConcert().size() == 0){
                            concertListener.onConcertAction(false);
                        }

                    }else{
                        imageSelected.setVisibility(View.VISIBLE);
                        concert.isSelected = true;
                        concertListener.onConcertAction(true);
                    }

                }
            });

        }
    }

}
