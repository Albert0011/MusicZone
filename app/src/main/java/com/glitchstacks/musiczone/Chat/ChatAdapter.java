package com.glitchstacks.musiczone.Chat;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.glitchstacks.musiczone.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolders>{
    private List<ChatObject> chatList;
    private Context context;


    public ChatAdapter(List<ChatObject> matchesList, Context context){
        this.chatList = matchesList;
        this.context = context;
    }

    @Override
    public ChatViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        ChatViewHolders rcv = new ChatViewHolders(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(ChatViewHolders holder, int position) {

        holder.mMessage.setText(chatList.get(position).getMessage());
        if(chatList.get(position).getCurrentUser()){
            holder.mContainer.setGravity(Gravity.END);
            holder.mUsername.setText("");
        }else{
            holder.mMessage.setBackground(ContextCompat.getDrawable(context,R.drawable.their_message));
            holder.mContainer.setGravity(Gravity.START);
            holder.mUsername.setText(chatList.get(position).getUsername());
        }

    }

    @Override
    public int getItemCount() {
        return this.chatList.size();
    }
}