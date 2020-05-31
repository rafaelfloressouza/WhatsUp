package com.rafaelfloressouza.whatsup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashBoardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Constants
    final int REQUEST_CODE = 123;


    // Variables will be used to connect code and layout.
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabItem mChatsItem, mGroupsItem, mCallsItem;
    private Toolbar toolbar;
    private PagerAdapter mPagerAdapter;


    // Variables for Recycler View inside Navigation View
    private RecyclerView mUserList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<User> userList, contactList; // Used to store all contacts on phone.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        // Initializing both lists to empty lists.
        contactList = new ArrayList<>();
        userList = new ArrayList<>();

        initializeRecyclerView(); // Setting up the Recycler View with all of its components.
        getContacts(); // Populating userList with all contacts on phone.

        getPermissions(); // Getting permission from user to access phone's contacts.

        // Setting up the toolbar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Setting up custom toolbar.

        // Connecting variables to layout components.
        connectVariablesToLayout();

        mNavigationView.setNavigationItemSelectedListener(this);
        mToggle = new ActionBarDrawerToggle(DashBoardActivity.this, mDrawerLayout, toolbar, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.setDrawerIndicatorEnabled(true);
        mToggle.syncState(); // Will take care of knowing if navigation view is closed/open.

        // Customizing the toolbar even more.
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_contacts);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mTabLayout.getTabCount());
        mViewPager.setAdapter(mPagerAdapter);


        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    }

    private void connectVariablesToLayout() {
        mViewPager = (ViewPager) findViewById(R.id.dash_board_view_pager);
        mTabLayout = (TabLayout) findViewById(R.id.dashboard_tab_layout);
        mChatsItem = (TabItem) findViewById(R.id.chats_item);
        mGroupsItem = (TabItem) findViewById(R.id.groups_item);
        mCallsItem = (TabItem) findViewById(R.id.calls_item);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dash_board_drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.dash_board_navigation_view);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out_item:
                // Login Out
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        logMeOut();
                    }
                }, 1000);
                break;
            case R.id.find_user_item:
                // Find
                findUser();
                break;
            default:
                // Just to be safe!
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.other_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        //TODO: Implement this portion once you figure out recycler view inside
//        mDrawerLayout.closeDrawer(Gravity.START, false);
        return false;
    }

    public void findUser() {
//        startActivity(new Intent(getApplicationContext(), FindUserActivity.class));
    }

    private void logMeOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(DashBoardActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

    // Getting permission to access contacts
    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE);
        }
    }

    private void getContacts() {

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        String countryISO = getCountryISO();

        while (phones.moveToNext()) { // While there is a contact
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            // Making sure the phone numbers will be in only one format
            phone = phone.replace(" ", "");
            phone = phone.replace("-", "");
            phone = phone.replace("(", "");
            phone = phone.replace(")", "");

            if (!String.valueOf(phone.charAt(0)).equals("+")) {
                phone = countryISO + phone;
            }

            // Adding new user from contacts into user list.
            User newContact = new User("",name, phone);
            contactList.add(newContact);
            //Updating the recycler view
            getUserDetails(newContact);
        }
        phones.close(); // Closing the phones Cursor.
    }

    private void getUserDetails(User newContact) {

        DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference().child("user");
        Query query = mDataBase.orderByChild("phone").equalTo(newContact.getPhone());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String phone = "", name = "";
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        if (childSnapshot.child("phone").getValue() != null) {
                            phone = childSnapshot.child("phone").getValue().toString();
                        }

                        if (childSnapshot.child("name").getValue() != null) {
                            name = childSnapshot.child("name").getValue().toString();
                        }

                        User mUser = new User(childSnapshot.getKey(), name, phone);
                        if (name.equals(phone)) {
                            for (User mContactIterator : contactList) {
                                if (mContactIterator.getPhone().equals(mUser.getPhone())) {
                                    mUser.setName(mContactIterator.getName());
                                }
                            }
                        }

                        userList.add(mUser);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private String getCountryISO() {
        String iso = null;

        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        if (telephonyManager.getNetworkCountryIso() != null) {

            if (!telephonyManager.getNetworkCountryIso().toString().equals("")) {
                iso = telephonyManager.getNetworkCountryIso().toString();
            }
        }

        return CountryToPhonePrefix.getPhone(iso);
    }


    private void initializeRecyclerView() {

        // Setting up Recycler View
        mUserList = findViewById(R.id.user_list);
        mUserList.setNestedScrollingEnabled(false); // Making the recycle view scroll seemlesly.
        mUserList.setHasFixedSize(false);

        //  Setting up Layout Manager
        mLayoutManager = new LinearLayoutManager(DashBoardActivity.this, RecyclerView.VERTICAL, false);
        mUserList.setLayoutManager(mLayoutManager);

        // Setting up the Adapter
        mAdapter = new UserListAdapter(userList);
        mUserList.setAdapter(mAdapter);
    }


}
