package com.glitchstacks.musiczone.BuyTicket;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.glitchstacks.musiczone.R;

import java.util.ArrayList;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketHolder> {


    ArrayList<TicketHelperClass> ticketLocations;
    View view;

    public TicketAdapter(ArrayList<TicketHelperClass> ticketLocations) {
        this.ticketLocations = ticketLocations;
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



    }

    @Override
    public int getItemCount() {
        return ticketLocations.size();
    }

    public class TicketHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView image;
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

        }

        @Override
        public void onClick(View view) {

        }
    }

}
