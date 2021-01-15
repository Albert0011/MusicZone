package com.glitchstacks.musiczone.Chat;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.utils.Utils;
import com.glitchstacks.musiczone.R;

public class ChatViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{
    public View avatar;
    public TextView mMessage, timeText, nameText;
    public LinearLayout mContainer;
    ImageView profileImage;

    public ChatViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mMessage = itemView.findViewById(R.id.message);
        mContainer = itemView.findViewById(R.id.container);
        timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        nameText = (TextView) itemView.findViewById(R.id.text_message_name);

    }


    @Override
    public void onClick(View view) {
    }
}