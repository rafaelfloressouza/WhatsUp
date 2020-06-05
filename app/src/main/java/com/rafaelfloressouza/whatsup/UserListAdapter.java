package com.rafaelfloressouza.whatsup;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {

    private ArrayList<User> userList;
    private Map<String, String> chatsCurrentUser = new HashMap<>();
    private Map<String, String> chatsOtherUser = new HashMap<>();

    public UserListAdapter(ArrayList<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, null, false);

        // Making sure that the view has has a width that matches the parent and height that wraps content.
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        // Getting the recycler View
        return new UserListViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder holder, final int position) {

        // Setting the text view's name and phone to the the corresponding user present in the userList.
        holder.mName.setText(userList.get(position).getName());
        holder.mPhone.setText(userList.get(position).getPhone());

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                final String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();
                final String currentUserId = FirebaseAuth.getInstance().getUid();
                final String otherUserId = userList.get(position).getUid();

                Query query = FirebaseDatabase.getInstance().getReference().child("user").child(currentUserId).child("chat");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        boolean otherUserFound = false;
                        if (snapshot.exists()) {

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Toast.makeText(v.getContext(), "HERE:" + dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();
                                if (dataSnapshot.getValue().equals(otherUserId)) {
                                    otherUserFound = true;
                                    break;
                                }
                            }
                        }

                        // Users do not have a chat open with each other.
                        if (!otherUserFound) {
                            Toast.makeText(v.getContext(), "Creating New Chat", Toast.LENGTH_SHORT).show();
                            FirebaseDatabase.getInstance().getReference().child("user").child(currentUserId).child("chat").child(key).setValue(otherUserId);
                            FirebaseDatabase.getInstance().getReference().child("user").child(otherUserId).child("chat").child(key).setValue(currentUserId);
                        } else { // Users already have a chat, so we go to their activity
                            Toast.makeText(v.getContext(), "Going to Chat Activity", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


//                if (chatsOtherUser.get(currentUserId) == null && chatsCurrentUser.get(otherUserId) == null) { // Users do not have a chat open with each other.
//
//                    Toast.makeText(v.getContext(), "Creating New Chat Activity", Toast.LENGTH_SHORT).show();
//                    chatsCurrentUser.put(otherUserId, key);
//                    chatsOtherUser.put(currentUserId, key);
//                    FirebaseDatabase.getInstance().getReference().child("user").child(currentUserId).child("chat").child(key).setValue(otherUserId);
//                    FirebaseDatabase.getInstance().getReference().child("user").child(otherUserId).child("chat").child(key).setValue(currentUserId);
//
//                } else if (chatsOtherUser.get(currentUserId).equals(chatsCurrentUser.get(otherUserId))) { // Users already have a chat, so we go to their chat activity.
//                    Toast.makeText(v.getContext(), "Going to Chat Activity", Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.userList.size();
    }

    public class UserListViewHolder extends RecyclerView.ViewHolder {

        public TextView mName, mPhone;
        LinearLayout mLayout;

        public UserListViewHolder(View view) {
            super(view);
            mName = view.findViewById(R.id.name);
            mPhone = view.findViewById(R.id.phone);
            mLayout = view.findViewById(R.id.user_item_layout);
        }
    }
}
