package com.rafaelfloressouza.whatsup.Activities;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rafaelfloressouza.whatsup.Adapters.MediaAdapter;
import com.rafaelfloressouza.whatsup.Utilities.ChatActivityItemDecorator;
import com.rafaelfloressouza.whatsup.Adapters.MessageAdapter;
import com.rafaelfloressouza.whatsup.Objects.MessageObject;
import com.rafaelfloressouza.whatsup.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    // Constants
    int PICK_IMAGE_INTENT = 1;

    // Toolbar variables
    private Toolbar chat_toolbar;

    // Variables to connect code to layout.
    EditText mMessage;
    ImageButton mSendMessage;
    ImageButton mAddMedia;
    TextView inChatOtherUserName;

    // // Variables for Recycler View Containing chats between two people.
    private RecyclerView mChat, mMedia;
    private RecyclerView.Adapter mChatAdapter, mMediaAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager, mMediaLayoutManager;

    ArrayList<MessageObject> messageList; // Used to store all messages.
    ArrayList<String> mediaURIList; // Used to store URIs for images.

    // Other Variables
    private String otherUserName; // Used to keep track of the other user's name to set the chat name.
    String chatID; // Used to keep track of the other chat ID that will connect to users.

    // Database variables
    DatabaseReference mChatDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        setUpToolBar();

        mediaURIList = new ArrayList<>();
        chatID = getIntent().getExtras().getString("chatID"); // Getting the chat id as an extra from other activity.

        mChatDB = FirebaseDatabase.getInstance().getReference().child("chat").child(chatID);

        mSendMessage = findViewById(R.id.send_button);
        mAddMedia = findViewById(R.id.media_button);
        mMessage = findViewById(R.id.message_input);

        initializeMessage();
        initializeMedia();
        getChatMessages();

        mMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendMessage();
                    handled = true;
                }
                return handled;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ChatActivity.this, DashBoardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    // onClickListener methods
    public void backButton(View view) {
        // onClickListener for back button on the chat's toolbar...
        startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));
        finish();
    }

    public void addMediaButton(View view) {
        // On Click listener for the "add media button".
        openGallery();
    }

    public void sendMessageButton(View view) {
        sendMessage();
    }

    // Set up methods
    public void setUpToolBar() {
        chat_toolbar = findViewById(R.id.in_chat_toolbar);
        setSupportActionBar(chat_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        inChatOtherUserName = findViewById(R.id.in_char_other_user_name);
        otherUserName = getIntent().getExtras().getString("otherUserName"); // Passing the name of the person we are chatting
        inChatOtherUserName.setText(otherUserName);
    }

    private void initializeMessage() {

        messageList = new ArrayList<>();

        // Setting up Recycler View
        mChat = findViewById(R.id.messages_recycler_view);

        // Setting up a decoration
        mChat.addItemDecoration(new ChatActivityItemDecorator(this.getApplicationContext()));

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

    private void getChatMessages() {

        mChatDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    String text = "", creatorID = "", sent_at = "";

                    if (dataSnapshot.child("text").getValue() != null) {
                        text = dataSnapshot.child("text").getValue().toString();
                    }
                    if (dataSnapshot.child("creator").getValue() != null) {
                        creatorID = dataSnapshot.child("creator").getValue().toString();
                    }

                    if (dataSnapshot.child("sent_at").getValue() != null) {
                        sent_at = dataSnapshot.child("sent_at").getValue().toString();
                    }

                    MessageObject mMessage = new MessageObject(dataSnapshot.getKey(), creatorID, text, sent_at);
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

    // Action methods
    private void sendMessage() {

        if (!mMessage.getText().toString().isEmpty()) { // If there is a message to send.
            DatabaseReference newMessageDB = mChatDB.push();
            Map<String, Object> newMessageMap = new HashMap<>();
            newMessageMap.put("text", mMessage.getText().toString());
            newMessageMap.put("creator", FirebaseAuth.getInstance().getUid());

            // Formmating the date
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy");
            LocalDateTime current_date = LocalDateTime.now();
            newMessageMap.put("sent_at", current_date.format(dtf));

            newMessageDB.updateChildren(newMessageMap);
        }
        mMessage.setText(null);
    }

    // Other methods
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
}
