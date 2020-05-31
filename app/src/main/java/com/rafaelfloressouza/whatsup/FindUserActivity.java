//package com.rafaelfloressouza.whatsup;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.database.Cursor;
//import android.os.Bundle;
//import android.provider.ContactsContract;
//import android.telephony.TelephonyManager;
//import android.util.Log;
//import android.widget.LinearLayout;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//
//public class FindUserActivity extends AppCompatActivity {
//
//    private RecyclerView mUserList;
//    private RecyclerView.Adapter mAdapter;
//    private RecyclerView.LayoutManager mLayoutManager;
//
//    ArrayList<User> userList, contactList; // Used to store all contacts on phone.
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_find_user);
//
//        contactList = new ArrayList<>();
//        userList = new ArrayList<>();
//
//        initializeRecyclerView(); // Setting up the Recycler View with all of its components.
//        getContacts(); // Populating userList with all contacts on phone.
//    }
//
//    private void getContacts() {
//
//        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
//        String countryISO = getCountryISO();
//
//        while (phones.moveToNext()) { // While there is a contact
//            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//            String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//
//            // Making sure the phone numbers will be in only one format
//            phone = phone.replace(" ", "");
//            phone = phone.replace("-", "");
//            phone = phone.replace("(", "");
//            phone = phone.replace(")", "");
//
//            if (!String.valueOf(phone.charAt(0)).equals("+")) {
//                phone = countryISO + phone;
//            }
//
//            // Adding new user from contacts into user list.
//            User newContact = new User("", name, phone);
//            contactList.add(newContact);
//            //Updating the recycler view
//            getUserDetails(newContact);
//        }
//        phones.close(); // Closing the phones Cursor.
//    }
//
//    private void getUserDetails(User newContact) {
//
//        DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference().child("user");
//        Query query = mDataBase.orderByChild("phone").equalTo(newContact.getPhone());
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    String phone = "", name = "";
//                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
//                        if (childSnapshot.child("phone").getValue() != null) {
//                            phone = childSnapshot.child("phone").getValue().toString();
//                        }
//
//                        if (childSnapshot.child("name").getValue() != null) {
//                            name = childSnapshot.child("name").getValue().toString();
//                        }
//
//                        User mUser = new User(childSnapshot.getKey(), name, phone);
//                        if (name.equals(phone)) {
//                            for (User mContactIterator : contactList) {
//                                if (mContactIterator.getPhone().equals(mUser.getPhone())) {
//                                    mUser.setName(mContactIterator.getName());
//                                }
//                            }
//                        }
//
//                        userList.add(mUser);
//                        mAdapter.notifyDataSetChanged();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }
//
//    private String getCountryISO() {
//        String iso = null;
//
//        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
//        if (telephonyManager.getNetworkCountryIso() != null) {
//
//            if (!telephonyManager.getNetworkCountryIso().toString().equals("")) {
//                iso = telephonyManager.getNetworkCountryIso().toString();
//            }
//        }
//
//        return CountryToPhonePrefix.getPhone(iso);
//    }
//
//
//    private void initializeRecyclerView() {
//
//        // Setting up Recycler View
//        mUserList = findViewById(R.id.user_list);
//        mUserList.setNestedScrollingEnabled(false); // Making the recycle view scroll seemlesly.
//        mUserList.setHasFixedSize(false);
//
//        //  Setting up Layout Manager
//        mLayoutManager = new LinearLayoutManager(FindUserActivity.this, RecyclerView.VERTICAL, false);
//        mUserList.setLayoutManager(mLayoutManager);
//
//        // Setting up the Adapter
//        mAdapter = new UserListAdapter(userList);
//        mUserList.setAdapter(mAdapter);
//    }
//}
