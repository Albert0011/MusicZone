package com.glitchstacks.musiczone.Chat;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.glitchstacks.musiczone.R;

public class ChatViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{
    public View avatar;
    public TextView mMessage, mUsername;
    public LinearLayout mContainer;
    public ChatViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

//        their_message = itemView.findViewById(R.id.their_message);
//        their_container = itemView.findViewById(R.id.their_container);
        mUsername = itemView.findViewById(R.id.username_txt);
        mMessage = itemView.findViewById(R.id.message);
        mContainer = itemView.findViewById(R.id.container);

    }



    @Override
    public void onClick(View view) {

    }
}