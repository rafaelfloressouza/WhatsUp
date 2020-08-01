package com.rafaelfloressouza.whatsup.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rafaelfloressouza.whatsup.Adapters.PagerAdapter;
import com.rafaelfloressouza.whatsup.Utilities.Contacts;
import com.rafaelfloressouza.whatsup.R;

public class DashBoardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Constants
    final int REQUEST_CODE = 123;

    // Variables for Tab Menu
//    private DrawerLayout mDrawerLayout;
//    private ActionBarDrawerToggle mToggle;
//    private NavigationView mNavigationView;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabItem mChatsItem, mGroupsItem, mCallsItem;
    private Toolbar toolbar;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        //Populating userList and nonUserList in GetUsersAndNonUsers class with the right contacts on phone
        Contacts.GetUsersAndNonUsers.setContext(getApplicationContext());
        Contacts.GetUsersAndNonUsers.getContacts();

        getPermissionToAccessContacts();

        // Setting up the toolbar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Connecting variables to layout components.
        connectVariablesToLayout();

        // In Case, a Navigation View will be added...
//        mNavigationView.setNavigationItemSelectedListener(this);
//        mToggle = new ActionBarDrawerToggle(DashBoardActivity.this, mDrawerLayout, toolbar, R.string.open, R.string.close);
//        mDrawerLayout.addDrawerListener(mToggle);
//        mToggle.setDrawerIndicatorEnabled(true);
//        mToggle.syncState(); // Will take care of knowing if navigation view is closed/open.

        // Customizing the toolbar even more.
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_users_base_green);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.log_out_item:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        logMeOut();
                    }
                }, 1000);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void connectVariablesToLayout() {
        mViewPager = (ViewPager) findViewById(R.id.dash_board_view_pager);
        mTabLayout = (TabLayout) findViewById(R.id.dashboard_tab_layout);
        mChatsItem = (TabItem) findViewById(R.id.chats_item);
//        mGroupsItem = (TabItem) findViewById(R.id.groups_item);
//        mCallsItem = (TabItem) findViewById(R.id.calls_item);
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.dash_board_drawer_layout);
//        mNavigationView = (NavigationView) findViewById(R.id.dash_board_navigation_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.other_menu, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    private void logMeOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(DashBoardActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

    private void getPermissionToAccessContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE);
        }
    }
}
