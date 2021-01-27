package com.glitchstacks.musiczone.AdminPage;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.glitchstacks.musiczone.R;
import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    ArrayList<Request> requestArrayList;
    View view;

    public RequestAdapter(ArrayList<Request> requests) {
        this.requestArrayList = requests;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_card, parent, false);
        RequestViewHolder requestViewHolder = new RequestViewHolder(view);
        return requestViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        Request request = requestArrayList.get(position);
        holder.bindRequest(request);
    }

    @Override
    public int getItemCount() {
        return requestArrayList.size();
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder {

        LinearLayout request_layout;
        TextView name;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);

            // Hooks
            name = itemView.findViewById(R.id.userID);
            request_layout = itemView.findViewById(R.id.card_layout);
        }

        void bindRequest(final Request request){

            name.setText(request.getUserID());

            request_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                Log.d("ClickedRequest", request.getUserID());

                Intent intent = new Intent(request_layout.getContext(), RequestDetail.class);

                intent.putExtra("userID", request.getUserID());

                request_layout.getContext().startActivity(intent);

                }
            });
        }
    }
}