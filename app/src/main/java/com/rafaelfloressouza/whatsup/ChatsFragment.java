package com.rafaelfloressouza.whatsup;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    View viewToInflate;

    private RecyclerView mChatList;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;

    ArrayList<Chat> chatList;

    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        this.viewToInflate = inflater.inflate(R.layout.chats_fragment, container, false);

        initializeRecyclerView(); // Initializing all variables.
        getChatList(); // Getting all chats for the respective user.

        return viewToInflate;
    }

    private void getChatList() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("chat");

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        Chat mChat = new Chat(childSnapshot.getKey());
                        boolean exists = false;
                        for (Chat chatIterator : chatList) { //TODO: Check this part to fix duplicate chats.
                            if (chatIterator.getChatId().equals(mChat.getChatId()))
                                exists = true;
                        }
                        if (exists)
                            continue;
                        chatList.add(mChat);
                        mChatAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initializeRecyclerView() {

        chatList = new ArrayList<>();

        // Setting up Recycler View
        mChatList = viewToInflate.findViewById(R.id.chat_list);
        mChatList.setNestedScrollingEnabled(false); // Making the recycle view scroll seemlesly.
        mChatList.setHasFixedSize(false);

        //  Setting up Layout Manager
        mChatLayoutManager = new LinearLayoutManager(viewToInflate.getContext(), RecyclerView.VERTICAL, false);
        mChatList.setLayoutManager(mChatLayoutManager);

        // Setting up the Adapter
        mChatAdapter = new ChatListAdapter(chatList);
        mChatList.setAdapter(mChatAdapter);
    }

}
