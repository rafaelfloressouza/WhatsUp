package com.rafaelfloressouza.whatsup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    // Variables to connect code to layout.
    EditText mMessage;
    Button mSendMessage;

    // // Variables for Recycler View Containing chats between two people.
    private RecyclerView mChat;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;

    ArrayList<MessageObject> messageList; // Used to store all messages.

    String chatID;
    DatabaseReference mChatDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatID = getIntent().getExtras().getString("chatID"); // Getting the chat id as an extra from other activity.
        mChatDB = FirebaseDatabase.getInstance().getReference().child("chat").child(chatID);

        mSendMessage = (Button) findViewById(R.id.send_button);
        mMessage = (EditText) findViewById(R.id.message_input);

        initializeRecyclerView();
        getChatMessages();
    }

    private void getChatMessages() {

        mChatDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    String text = "", creatorID = "";

                    if(dataSnapshot.child("text").getValue() != null){
                        text = dataSnapshot.child("text").getValue().toString();
                    }
                    if(dataSnapshot.child("creator").getValue() != null){
                        creatorID = dataSnapshot.child("creator").getValue().toString();
                    }

                    MessageObject mMessage = new MessageObject(dataSnapshot.getKey(), creatorID, text);
                    messageList.add(mMessage);
                    mChatLayoutManager.scrollToPosition(messageList.size() - 1); // Scrolls to last chat elements.
                    mChatAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void sendMessageButton(View view){
//        Toast.makeText(this, FirebaseAuth.getInstance().getUid(), Toast.LENGTH_SHORT).show();
        sendMessage();
    }

    private void sendMessage() {

        if(!mMessage.getText().toString().isEmpty()){ // If there is a message to send.
            DatabaseReference newMessageDB =  mChatDB.push();

            Map<String, Object> newMessageMap = new HashMap<>();
            newMessageMap.put("text", mMessage.getText().toString());
            newMessageMap.put("creator", FirebaseAuth.getInstance().getUid());
            newMessageDB.updateChildren(newMessageMap);
        }
        mMessage.setText(null);
    }

    private void initializeRecyclerView() {

        messageList = new ArrayList<>();

        // Setting up Recycler View
        mChat = findViewById(R.id.messages_recycler_view);
        mChat.setNestedScrollingEnabled(false); // Making the recycle view scroll seemlesly.
        mChat.setHasFixedSize(false);

        //  Setting up Layout Manager
        mChatLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        mChat.setLayoutManager(mChatLayoutManager);

        // Setting up the Adapter
        mChatAdapter = new MessageAdapter(messageList);
        mChat.setAdapter(mChatAdapter);
    }
}
