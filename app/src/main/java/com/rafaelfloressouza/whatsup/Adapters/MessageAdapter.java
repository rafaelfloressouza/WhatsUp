package com.rafaelfloressouza.whatsup.Adapters;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.rafaelfloressouza.whatsup.Objects.MessageObject;
import com.rafaelfloressouza.whatsup.R;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private ArrayList<MessageObject> messageList;

    public MessageAdapter(ArrayList<MessageObject> messageObjectList) {
        this.messageList = messageObjectList;
    }

    // ViewHolder for messages..
    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView mMessage, sent_at;
        LinearLayout mLayout;
        LinearLayout inChatLayout;

        MessageViewHolder(View view) {
            super(view);
            mMessage = view.findViewById(R.id.in_chat_message);
            mLayout = view.findViewById(R.id.linear_layout_message_item);
            inChatLayout = view.findViewById(R.id.in_chat_linear_layout);
            sent_at = view.findViewById(R.id.in_chat_sent_at);
        }
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
        holder.sent_at.setText(messageList.get(position).getSentAt());

        if (!messageList.get(position).getSenderId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

            // Some else's message
            holder.mLayout.setGravity(Gravity.LEFT);

            if (holder.mMessage.length() <= 7) {
                holder.inChatLayout.getLayoutParams().width = 220;
            } else {
                holder.inChatLayout.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
            }

            holder.inChatLayout.setBackgroundResource(R.drawable.incoming_speech_bubble);
            holder.inChatLayout.setPadding(45, 4, 12, 10);
        } else {

            // Message send by the owner of the phone

            holder.mLayout.setGravity(Gravity.RIGHT);

            if (holder.mMessage.length() <= 7) {
                holder.inChatLayout.getLayoutParams().width = 220;
            } else {
                holder.inChatLayout.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
            }

            holder.inChatLayout.setBackgroundResource(R.drawable.outgoing_speech_bubble);
            holder.inChatLayout.setPadding(12, 4, 45, 10);
        }
    }

    @Override
    public int getItemCount() {
        return this.messageList.size();
    }
}
