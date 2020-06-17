package com.rafaelfloressouza.whatsup;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.renderscript.Sampler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    View viewToInflate;

    private RecyclerView mChatList;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;

    ArrayList<Chat> chatList;
    Map<String, String> userMap;

    FloatingActionButton newChatButton;


    public ChatsFragment(Map<String, String> userMap) {
        this.userMap = userMap;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.viewToInflate = inflater.inflate(R.layout.chats_fragment, container, false);

        newChatButton = viewToInflate.findViewById(R.id.new_chat_button);

        newChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(viewToInflate.getContext(), "Pressed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), CreateNewChatActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("chatID",chatList.get(holder.getAdapterPosition()).getChatId());
//                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });

        initializeRecyclerView(); // Initializing all variables.
        getChatList(); // Getting all chats for the respective user.

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