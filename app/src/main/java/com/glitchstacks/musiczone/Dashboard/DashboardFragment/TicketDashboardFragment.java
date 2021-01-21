package com.glitchstacks.musiczone.Dashboard.DashboardFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.glitchstacks.musiczone.Database.SessionManager;
import com.glitchstacks.musiczone.BuyTicket.TicketAdapter;
import com.glitchstacks.musiczone.BuyTicket.TicketHelperClass;
import com.glitchstacks.musiczone.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class TicketDashboardFragment extends Fragment {

    private RecyclerView ticketRecycler;
    private RecyclerView.Adapter ticketAdapter;

    private DatabaseReference mDatabase;

    private ArrayList<TicketHelperClass> ticketList = new ArrayList<TicketHelperClass>();

    private String phoneNumber;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_ticket_dashboard, container, false);

        // Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        SessionManager sessionManager = new SessionManager(getContext(), SessionManager.SESSION_USERSESSION);

        // User Information map
        HashMap<String, String> map = sessionManager.getUsersDetailFromSession();

        // UserID
        phoneNumber = map.get(SessionManager.KEY_PHONENUMBER);

        // List of Tickets
        ticketRecycler = root.findViewById(R.id.ticket_recycler);
        ticketRecycler();

        ticketAdapter = new TicketAdapter(getTicket());
        ticketRecycler.setAdapter(ticketAdapter);

        return root;
    }

    private void ticketRecycler(){

        ticketRecycler.setHasFixedSize(true);
        ticketRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        mDatabase.child("Users").child(phoneNumber).child("tickets").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){

                    Log.d("child detected", "child detected");

                    final String[] imageUrl = {null};
                    final String[] concertTitle = { null };
                    final String[] concertDesc = { null };
                    final String[] concertDate = { null };
                    final String[] concertTime = { null };
                    String concertKey = null;
                    String amountTicket = null;
                    String area = null;
                    Integer viewer;

                    // Hook from database
                    concertKey = snapshot.child("concert_key").getValue().toString();
                    amountTicket = snapshot.child("amountTicket").getValue().toString();
                    area = snapshot.child("area").getValue().toString();

                    final String finalAmountTicket = amountTicket;
                    final String finalArea = area;
                    mDatabase.child("Concerts").child(concertKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            imageUrl[0] = snapshot.child("imageURL").getValue().toString();
                            concertTitle[0] = snapshot.child("concert_name").getValue().toString();
                            concertDesc[0] = snapshot.child("description").getValue().toString();
                            concertDate[0] = snapshot.child("date").getValue().toString();
                            concertTime[0] = snapshot.child("time").getValue().toString();


                            if(!imageUrl[0].isEmpty() && !concertTitle[0].isEmpty() && !concertDesc[0].isEmpty() && !concertDate[0].isEmpty()
                                    && !finalArea.isEmpty() && !finalAmountTicket.isEmpty() && !concertTime[0].isEmpty() && !finalAmountTicket.isEmpty()){

                                TicketHelperClass ticketHelperClass = new TicketHelperClass(imageUrl[0], concertTitle[0], concertDesc[0], concertDate[0], concertTime[0], finalArea, finalAmountTicket);

                                ticketList.add(ticketHelperClass);
                                ticketAdapter.notifyDataSetChanged();

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });






                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



    }


    private ArrayList<TicketHelperClass> getTicket(){
        return ticketList;
    }

}