package com.rafaelfloressouza.whatsup;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {

    private ArrayList<Chat> chatList;

    public ChatListAdapter(ArrayList<Chat> chatList) {
        this.chatList = chatList;
    }

    // ViewHolder for the chats
    public class ChatListViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;
        LinearLayout mLayout;

        public ChatListViewHolder(View view) {
            super(view);

            mTitle = view.findViewById(R.id.chat_title);
            mLayout = view.findViewById(R.id.chat_item_layout);
        }
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, null, false);

        // Making sure that the view has has a width that matches the parent and height that wraps content.
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        return new ChatListViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatListViewHolder holder, final int position) {

        // Setting respective Chat's name and picture
        holder.mTitle.setText(chatList.get(position).getName());

        // Used to take us to the right chat...
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("chatID", chatList.get(holder.getAdapterPosition()).getChatId());
                bundle.putString("otherUserName", chatList.get(position).getName());
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.chatList.size();
    }
}
