package com.rafaelfloressouza.whatsup.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rafaelfloressouza.whatsup.Adapters.ChatListAdapter;
import com.rafaelfloressouza.whatsup.Utilities.Contacts;
import com.rafaelfloressouza.whatsup.Objects.Chat;
import com.rafaelfloressouza.whatsup.Activities.CreateNewChatActivity;
import com.rafaelfloressouza.whatsup.R;
import com.rafaelfloressouza.whatsup.Objects.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatsFragment extends Fragment {

    View viewToInflate;

    // Variables for setting up the chats recycler view
    private RecyclerView mChatList;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;

    // Data structures to store chats, users, nonuser.
    private ArrayList<Chat> chatList;
    private ArrayList<User> userList;
    private Map<String, String> userMap;
    private ArrayList<User> nonUserList;

    // Buttons
    private FloatingActionButton newChatButton;

    public ChatsFragment() {
        this.userMap = Contacts.GetUsersAndNonUsers.getUserMap();
        this.userList = Contacts.GetUsersAndNonUsers.getUserList();
        this.nonUserList = Contacts.GetUsersAndNonUsers.getNonUserList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.viewToInflate = inflater.inflate(R.layout.chats_fragment, container, false);

        newChatButton = viewToInflate.findViewById(R.id.new_chat_button);

        // Takes us to create a new chat or go to an existing chat.
        newChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CreateNewChatActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                v.getContext().startActivity(intent);
            }
        });

        initializeRecyclerView();
        getChatList();

        return viewToInflate;
    }


    // Method used to populate array lists.
    private void getChatList() {
        DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("chat");
        mDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot userChat) {

                if (userChat.exists()) {

                    for (final DataSnapshot chatSnapshot : userChat.getChildren()) {
                        String otherUserId = chatSnapshot.getValue().toString();
                        Query q = FirebaseDatabase.getInstance().getReference().child("user").child(otherUserId).child("phone");
                        q.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshotII) {

                                Chat mChat = new Chat(chatSnapshot.getKey(), userMap.get(snapshotII.getValue().toString()));
                                chatList.add(mChat);
                                mChatAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Initializer Methods
    private void initializeRecyclerView() {

        chatList = new ArrayList<>();


        // Setting up Recycler View
        mChatList = viewToInflate.findViewById(R.id.chat_list);
        mChatList.setNestedScrollingEnabled(false); // Making the recycle view scroll seemlesly.
        mChatList.setHasFixedSize(false);

        // Setting up a divider in the recycler view.
        RecyclerView.ItemDecoration divider = new DividerItemDecoration(viewToInflate.getContext(), DividerItemDecoration.VERTICAL);
        mChatList.addItemDecoration(divider);


        //  Setting up Layout Manager
        mChatLayoutManager = new LinearLayoutManager(viewToInflate.getContext(), RecyclerView.VERTICAL, false);
        mChatList.setLayoutManager(mChatLayoutManager);

        // Setting up the Adapter
        mChatAdapter = new ChatListAdapter(chatList);
        mChatList.setAdapter(mChatAdapter);
    }

}
