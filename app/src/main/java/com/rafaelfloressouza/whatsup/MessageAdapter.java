package com.rafaelfloressouza.whatsup;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private ArrayList<MessageObject> messageList;

    public MessageAdapter(ArrayList<MessageObject> messageObjectList) {
        this.messageList = messageObjectList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, null, false);

        // Making sure that the view has has a width that matches the parent and height that wraps content.
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        // Getting the recycler View
        return new MessageViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, final int position) {

        holder.mMessage.setText(messageList.get(position).getMessage());
        holder.mSender.setText(messageList.get(position).getSenderId());
    }

    @Override
    public int getItemCount() {
        return this.messageList.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView mMessage, mSender;
        LinearLayout mLayout;

         MessageViewHolder(View view) {
            super(view);
             mMessage = view.findViewById(R.id.in_chat_message);
             mSender = view.findViewById(R.id.in_chat_sender);
        }
    }
}
