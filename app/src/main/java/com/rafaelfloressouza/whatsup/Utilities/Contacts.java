package com.rafaelfloressouza.whatsup.Utilities;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rafaelfloressouza.whatsup.Objects.User;
import com.rafaelfloressouza.whatsup.Utilities.CountryToPhonePrefix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Contacts {

    public static class GetUsersAndNonUsers {

        private static Context context = null;
        private static Map<String, String> userMap;
        private static ArrayList<User> userList;
        private static ArrayList<User> nonUserList;

        static{
            userMap = new HashMap<>();
            userList = new ArrayList<>();
            nonUserList = new ArrayList<>();
        }

        public static void getContacts() {

            if(isContextNull()) // Checking if a context was provided, otherwise nullptr exception will be thrown.
                throwError();

            userMap = new HashMap<>();
            userList = new ArrayList<>();
            nonUserList = new ArrayList<>();

            Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
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

                User newContact = new User("", name, phone);
                userMap.put(phone, name);

                getUserDetails(newContact);
            }
            phones.close(); // Closing the phones Cursor.
        }

        private static void getUserDetails(final User newContact) {

            final DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference().child("user");
            final Query query = mDataBase.orderByChild("phone").equalTo(newContact.getPhone());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) { // User found on Firebase....

                        String phone = "", name = "", userId = "";

                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                            if (childSnapshot.exists()) {

                                userId = childSnapshot.getKey();

                                if (childSnapshot.child("phone").getValue() != null) {
                                    phone = childSnapshot.child("phone").getValue().toString();
                                }
                                userList.add(new User(userId, userMap.get(phone), phone));
                            }
                        }
                    } else { // User not found on Firebase
                        nonUserList.add(newContact);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        private static String getCountryISO() {
            String iso = null;

            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            if (telephonyManager.getNetworkCountryIso() != null) {

                if (!telephonyManager.getNetworkCountryIso().toString().equals("")) {
                    iso = telephonyManager.getNetworkCountryIso().toString();
                }
            }

            return CountryToPhonePrefix.getPhone(iso);
        }

        public static void setContext(Context new_context){
            context = new_context;
        }

        public static boolean isContextNull(){
            return context == null;
        }

        public static String getContext(){
            if(context == null){
                return "Context is Null";
            }else{
                return context.toString();
            }
        }

        public static Map<String, String> getUserMap(){
            return userMap;
        }

        public static ArrayList<User> getUserList(){
            return userList;
        }

        public static ArrayList<User> getNonUserList(){
            return nonUserList;
        }

        private static void throwError(){
            throw new NullPointerException("You need to provide a context in order to get contacts (Users and Non-Users)");
        }
    }
}
