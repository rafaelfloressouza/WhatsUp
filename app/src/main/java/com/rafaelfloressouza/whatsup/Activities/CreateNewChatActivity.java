package com.rafaelfloressouza.whatsup.Activities;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.rafaelfloressouza.whatsup.Adapters.NonUserListAdapter;
import com.rafaelfloressouza.whatsup.Adapters.UserListAdapter;
import com.rafaelfloressouza.whatsup.Objects.User;
import com.rafaelfloressouza.whatsup.R;
import com.rafaelfloressouza.whatsup.Utilities.Contacts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateNewChatActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private EditText mSearchEditText;


    private ArrayList<User> userList = new ArrayList<>();
    private ArrayList<User> nonUserList = new ArrayList<>();
    private Map<String, String> userMap = new HashMap<>();

    // Variables for Recycler View inside Navigation View Containing Users of the App
    private RecyclerView mUserList;
    private RecyclerView.Adapter mUserListAdapter;
    private RecyclerView.LayoutManager mUserLayoutManager;

    // Variables for Recycler View inside Navigation View Containing Non-Users of the App
    private RecyclerView mNonUserList;
    private RecyclerView.Adapter mNonUserListAdapter;
    private RecyclerView.LayoutManager mNonUserLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_chat);

        initializeUserRecyclerView();
        initializeNonUserRecyclerView();

        setUpToolbar();

        mUserListAdapter.notifyDataSetChanged();
        mNonUserListAdapter.notifyDataSetChanged();

        // Listener for back button on toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateNewChatActivity.this, DashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        mSearchEditText = findViewById(R.id.search_edit_text);
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                filter(s.toString())
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CreateNewChatActivity.this, DashBoardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    // Initializer Methods
    private void setUpToolbar(){
        toolbar = (Toolbar) findViewById(R.id.create_new_chat_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initializeNonUserRecyclerView() {

        nonUserList = Contacts.GetUsersAndNonUsers.getNonUserList();

        // Setting up Recycler View
        mNonUserList = findViewById(R.id.non_user_list);
        mNonUserList.setNestedScrollingEnabled(false); // Making the recycler view scroll seemlesly.
        mNonUserList.setHasFixedSize(false);

        // Setting up Layout Manager
        mNonUserLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        mNonUserList.setLayoutManager(mNonUserLayoutManager);

        // Setting up the Adapter
        mNonUserListAdapter = new NonUserListAdapter(nonUserList);
        mNonUserList.setAdapter(mNonUserListAdapter);
    }

    private void initializeUserRecyclerView() {

        userList = Contacts.GetUsersAndNonUsers.getUserList();
        userMap = Contacts.GetUsersAndNonUsers.getUserMap();

        // Setting up Recycler View
        mUserList = findViewById(R.id.user_list);
        mUserList.setNestedScrollingEnabled(false); // Making the recycler view scroll seemlesly.
        mUserList.setHasFixedSize(false);

        //  Setting up Layout Manager
        mUserLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        mUserList.setLayoutManager(mUserLayoutManager);

        // Setting up the Adapter
        mUserListAdapter = new UserListAdapter(userList, userMap);
        mUserList.setAdapter(mUserListAdapter);
    }
}
