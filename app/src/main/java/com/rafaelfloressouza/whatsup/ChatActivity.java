package com.rafaelfloressouza.whatsup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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

    // Constants
    int PICK_IMAGE_INTENT = 1;

    // Variables to connect code to layout.
    EditText mMessage;
    Button mSendMessage;
    Button mAddMedia;

    // // Variables for Recycler View Containing chats between two people.
    private RecyclerView mChat, mMedia;
    private RecyclerView.Adapter mChatAdapter, mMediaAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager, mMediaLayoutManager;

    ArrayList<MessageObject> messageList; // Used to store all messages.
    ArrayList<String> mediaURIList; // Used to store URIs for images.

    String chatID;
    DatabaseReference mChatDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mediaURIList = new ArrayList<>();

        chatID = getIntent().getExtras().getString("chatID"); // Getting the chat id as an extra from other activity.
        mChatDB = FirebaseDatabase.getInstance().getReference().child("chat").child(chatID);

        mSendMessage = (Button) findViewById(R.id.send_button);
        mAddMedia = (Button) findViewById(R.id.media_button);
        mMessage = (EditText) findViewById(R.id.message_input);

        initializeMessage();
        initializeMedia();
        getChatMessages();
    }

    private void getChatMessages() {

        mChatDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    String text = "", creatorID = "";

                    if (dataSnapshot.child("text").getValue() != null) {
                        text = dataSnapshot.child("text").getValue().toString();
                    }
                    if (dataSnapshot.child("creator").getValue() != null) {
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

    public void addMediaButton(View view) {
        openGallery();
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture(s)"), PICK_IMAGE_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_INTENT) {
                if (data.getClipData() == null) { // User only picked one image.
                    mediaURIList.add(data.getData().toString());
                } else { // If user picks multiple images.
                    for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                        mediaURIList.add(data.getClipData().getItemAt(i).getUri().toString());
                    }
                }
                mMediaAdapter.notifyDataSetChanged();
            }
        }
    }

    public void sendMessageButton(View view) {
        sendMessage();
    }

    private void sendMessage() {

        if (!mMessage.getText().toString().isEmpty()) { // If there is a message to send.
            DatabaseReference newMessageDB = mChatDB.push();

            Map<String, Object> newMessageMap = new HashMap<>();
            newMessageMap.put("text", mMessage.getText().toString());
            newMessageMap.put("creator", FirebaseAuth.getInstance().getUid());
            newMessageDB.updateChildren(newMessageMap);
        }
        mMessage.setText(null);
    }

    private void initializeMessage() {

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

    private void initializeMedia() {

        // Setting up Recycler View
        mMedia = findViewById(R.id.media_list);
        mMedia.setNestedScrollingEnabled(false); // Making the recycle view scroll seemlesly.
        mMedia.setHasFixedSize(false);

        //  Setting up Layout Manager
        mMediaLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        mMedia.setLayoutManager(mMediaLayoutManager);

        // Setting up the Adapter
        mMediaAdapter = new MediaAdapter(getApplicationContext(), mediaURIList);
        mMedia.setAdapter(mMediaAdapter);
    }
}
