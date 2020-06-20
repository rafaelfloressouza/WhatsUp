package com.rafaelfloressouza.whatsup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import java.util.ArrayList;

public class CreateNewChatActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private ArrayList<User> userList = new ArrayList<>();
    private ArrayList<User> nonUserList = new ArrayList<>();

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

        userList = getIntent().getParcelableArrayListExtra("userList"); // Getting userList from chats fragment activity
        nonUserList = getIntent().getParcelableArrayListExtra("nonUserList"); // Getting nonUserList from chat fragment activity

        initializeUserRecyclerView();
        initializeNonUserRecyclerView();

        setUpToolbar();

        mUserListAdapter.notifyDataSetChanged();
        mNonUserListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.create_new_chat_menu, menu);
        return super.onCreateOptionsMenu(menu);
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

        // Setting up Recycler View
        mUserList = findViewById(R.id.user_list);
        mUserList.setNestedScrollingEnabled(false); // Making the recycler view scroll seemlesly.
        mUserList.setHasFixedSize(false);

        //  Setting up Layout Manager
        mUserLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        mUserList.setLayoutManager(mUserLayoutManager);

        // Setting up the Adapter
        mUserListAdapter = new UserListAdapter(userList);
        mUserList.setAdapter(mUserListAdapter);
    }
}
