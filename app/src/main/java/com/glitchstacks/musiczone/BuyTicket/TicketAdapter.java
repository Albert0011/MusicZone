package com.glitchstacks.musiczone.BuyTicket;

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
import com.glitchstacks.musiczone.PostConcert.Artist;
import com.glitchstacks.musiczone.R;

import java.util.ArrayList;
import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketHolder> {

    ArrayList<TicketHelperClass> ticketLocations;
    TicketListener ticketListener;
    View view;

    public TicketAdapter(ArrayList<TicketHelperClass> ticketLocations, TicketListener ticketListener) {
        this.ticketLocations = ticketLocations;
        this.ticketListener = ticketListener;
    }

    @NonNull
    @Override
    public TicketAdapter.TicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_card_design, parent,false);

        TicketAdapter.TicketHolder ticketHolder = new TicketAdapter.TicketHolder(view);
        return ticketHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TicketHolder holder, int position) {
        TicketHelperClass ticketHelperClass = ticketLocations.get(position);

        String profileUrl = ticketHelperClass.getImage();

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

        holder.amount.setText(ticketHelperClass.getAmount());
        holder.title.setText(ticketHelperClass.getTitle());
        holder.desc.setText(ticketHelperClass.getDesc());
        holder.date.setText(ticketHelperClass.getDate());
        holder.time.setText(ticketHelperClass.getTime());
        holder.area.setText(ticketHelperClass.getArea());
        holder.phoneNumber.setText(ticketHelperClass.getPhoneNumber());

        holder.bindTicket(ticketHelperClass);


    }

    public List<TicketHelperClass> getSelectedTicket(){
        List<TicketHelperClass> selectedTicket = new ArrayList<>();

        for(TicketHelperClass ticket : ticketLocations){

            if(ticket.isSelected){
                selectedTicket.add(ticket);
            }
        }
        return selectedTicket;
    }

    @Override
    public int getItemCount() {
        return ticketLocations.size();
    }

    public class TicketHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        LinearLayout ticket_layout;
        public ImageView image, imageSelected;
        public TextView title, desc, date, time, area, amount, phoneNumber;
        public TicketHolder(@NonNull View itemView) {
            super(itemView);

            // Hooks
            itemView.setOnClickListener(this);
            image = itemView.findViewById(R.id.ticket_image);
            title = itemView.findViewById(R.id.ticket_title);
            desc = itemView.findViewById(R.id.ticket_desc);
            date = itemView.findViewById(R.id.txtDate);
            time = itemView.findViewById(R.id.txtTime);
            area = itemView.findViewById(R.id.txtArea);
            amount = itemView.findViewById(R.id.amount);
            phoneNumber = itemView.findViewById(R.id.contactNumber);
            ticket_layout = itemView.findViewById(R.id.ticketDesign);
            imageSelected = itemView.findViewById(R.id.imageSelected);

        }

        @Override
        public void onClick(View view) {

        }

        void bindTicket(final TicketHelperClass ticket){

            if(ticket.isSelected){
                imageSelected.setVisibility(View.VISIBLE);
            }else{
                imageSelected.setVisibility(View.GONE);
            }


            ticket_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d("artistClickedBind", ticket.getTitle());

                    if(ticket.isSelected){
                        imageSelected.setVisibility(View.GONE);
                        ticket.isSelected = false;

                        if(getSelectedTicket().size() == 0){
                            ticketListener.onTicketAction(false);
                        }

                    }else{
                        imageSelected.setVisibility(View.VISIBLE);
                        ticket.isSelected = true;
                        ticketListener.onTicketAction(true);
                    }
                }
            });

            ticket_layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    return true;
                }
            });

        }

    }

}
