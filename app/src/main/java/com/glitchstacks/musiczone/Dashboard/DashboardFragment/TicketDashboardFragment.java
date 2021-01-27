package com.glitchstacks.musiczone.Dashboard.DashboardFragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.glitchstacks.musiczone.BuyTicket.TicketListener;
import com.glitchstacks.musiczone.Database.SessionManager;
import com.glitchstacks.musiczone.BuyTicket.TicketAdapter;
import com.glitchstacks.musiczone.BuyTicket.TicketHelperClass;
import com.glitchstacks.musiczone.PostConcert.AddPlaylist2;
import com.glitchstacks.musiczone.PostConcert.Artist;
import com.glitchstacks.musiczone.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TicketDashboardFragment extends Fragment implements TicketListener {

    private RecyclerView ticketRecycler;
    private TicketAdapter ticketAdapter;

    private DatabaseReference mDatabase;

    private ArrayList<TicketHelperClass> ticketList = new ArrayList<TicketHelperClass>();

    private String phoneNumber, promotorPhone;
    private Button finishButton;




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

        finishButton = root.findViewById(R.id.finishConcert);

        // List of Tickets
        ticketRecycler = root.findViewById(R.id.ticket_recycler);
        ticketRecycler();

        ticketAdapter = new TicketAdapter(getTicket(), this);
        ticketRecycler.setAdapter(ticketAdapter);

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("CekButton", "masuk bro");
                finishConcert();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TicketDashboardFragment()).commit();
                Toast.makeText(getContext(), "Ticket is now finished!", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    private void finishConcert() {

        if (ticketAdapter == null) {
            Toast.makeText(getContext(), "No ticket!", Toast.LENGTH_SHORT).show();
            return;
        }


        List<TicketHelperClass> selectedTicket = ticketAdapter.getSelectedTicket();
        Integer selectedTicketCount = selectedTicket.size();

        if (selectedTicketCount == 0) {
            Toast.makeText(getContext(), "Please select a Ticket first", Toast.LENGTH_SHORT).show();
            return;
        }

        for (TicketHelperClass ticket : selectedTicket) {
            Log.d("CekButton2", "masuk brok");
            String key = ticket.getConcertKey();
            mDatabase.child("Users").child(phoneNumber).child("tickets").child(key).child("status").setValue("finished");
        }

    }


    private void ticketRecycler(){



        ticketRecycler.setHasFixedSize(true);
        ticketRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        mDatabase.child("Users").child(phoneNumber).child("tickets").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){

                    Log.d("child detected", "child detected");

                    if(snapshot.child("status").getValue().toString() == "true"){

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
                        final String finalConcertKey = snapshot.getKey().toString();

                        mDatabase.child("Concerts").child(concertKey).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                imageUrl[0] = snapshot.child("imageURL").getValue().toString();
                                concertTitle[0] = snapshot.child("concert_name").getValue().toString();
                                concertDesc[0] = snapshot.child("description").getValue().toString();
                                concertDate[0] = snapshot.child("date").getValue().toString();
                                concertTime[0] = snapshot.child("time").getValue().toString();
                                promotorPhone = snapshot.child("promotor").getValue().toString();

//                                mDatabase.child("Users").child(promotorPhone[0]).addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot2) {
//                                        Log.d("CEKEMAIL","masuk");
//                                        email = snapshot2.child("email").getValue().toString();
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                    }
//                                });
//                                Log.d("CEKEMAIL",email);


                                if(!imageUrl[0].isEmpty() && !concertTitle[0].isEmpty() && !concertDesc[0].isEmpty() && !concertDate[0].isEmpty()
                                        && !finalArea.isEmpty() && !finalAmountTicket.isEmpty() && !concertTime[0].isEmpty() && !finalAmountTicket.isEmpty()){

                                    TicketHelperClass ticketHelperClass = new TicketHelperClass(imageUrl[0], concertTitle[0], concertDesc[0], concertDate[0], concertTime[0], finalArea, finalAmountTicket, promotorPhone, finalConcertKey);

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

    @Override
    public void onTicketAction(Boolean isSelected) {
//        Toast.makeText(getContext(), "Something Selected", Toast.LENGTH_SHORT).show();
    }
}