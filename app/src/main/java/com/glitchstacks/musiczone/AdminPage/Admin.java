package com.glitchstacks.musiczone.AdminPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.glitchstacks.musiczone.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Admin extends AppCompatActivity {

    private RecyclerView requestRecylerView;
    private ArrayList<Request> requestArrayList = new ArrayList<>();
    private RequestAdapter requestAdapter;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        requestRecylerView = findViewById(R.id.recyclerViewRequest);
        backButton = findViewById(R.id.back_btn);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        DatabaseReference mRequest = FirebaseDatabase.getInstance().getReference().child("Request");
        mRequest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    Log.d("requestExist", "true");

                    for(DataSnapshot s : snapshot.getChildren()){

                        Log.d("requestChild", s.getKey());

                        if(s.child("accepted").getValue().toString().equals("pending")){
                            Request currentRequest = new Request(s.getKey());
                            requestArrayList.add(currentRequest);
                        }
                    }

                    requestAdapter = new RequestAdapter(requestArrayList);
                    requestRecylerView.setHasFixedSize(true);
                    requestRecylerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL));
                    requestRecylerView.setAdapter(requestAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}