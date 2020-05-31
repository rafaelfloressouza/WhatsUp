package com.rafaelfloressouza.whatsup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {

    private ArrayList<User> userList;

    public UserListAdapter(ArrayList<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, null, false);

        // Making sure that the view has has a width that matches the parent and height that wraps content.
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        // Getting the recycler View
        return new UserListViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder holder, final int position) {

        // Setting the text view's name and phone to the the corresponding user present in the userList.
        holder.mName.setText(userList.get(position).getName());
        holder.mPhone.setText(userList.get(position).getPhone());

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();
                String currentUserId = FirebaseAuth.getInstance().getUid();
                String otherUserId = userList.get(position).getUid();

                FirebaseDatabase.getInstance().getReference().child("user").child(currentUserId).child("chat").child(key).setValue(true);
                FirebaseDatabase.getInstance().getReference().child("user").child(otherUserId).child("chat").child(key).setValue(true);

                //TODO: Erase this once debugging process is finished
//                Toast.makeText(v.getContext(),"cUID: " + currentUserId + " | oUID: " + otherUserId,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.userList.size();
    }

    public class UserListViewHolder extends RecyclerView.ViewHolder {

        public TextView mName, mPhone;
        LinearLayout mLayout;

        public UserListViewHolder(View view) {
            super(view);
            mName = view.findViewById(R.id.name);
            mPhone = view.findViewById(R.id.phone);
            mLayout = view.findViewById(R.id.user_item_layout);
        }
    }
}
