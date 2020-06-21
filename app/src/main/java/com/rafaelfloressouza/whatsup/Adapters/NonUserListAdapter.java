package com.rafaelfloressouza.whatsup.Adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rafaelfloressouza.whatsup.R;
import com.rafaelfloressouza.whatsup.Objects.User;

import java.util.ArrayList;

public class NonUserListAdapter extends RecyclerView.Adapter<NonUserListAdapter.UserListViewHolder> {

    private ArrayList<User> nonUserList;

    public NonUserListAdapter(ArrayList<User> nonUserList) {
        this.nonUserList = nonUserList;
    }

    // View holder for Non-Users
    public static class UserListViewHolder extends RecyclerView.ViewHolder {

        public TextView mName, mPhone;
        public Button mButton;
        public LinearLayout mLayout;

        public UserListViewHolder(View view) {
            super(view);
            mName = view.findViewById(R.id.non_user_name);
            mPhone = view.findViewById(R.id.non_user_phone);
            mLayout = view.findViewById(R.id.non_user_item_layout);
            mButton = view.findViewById(R.id.selection_button);
        }
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.non_user_item, null, false);

        // Making sure that the view has has a width that matches the parent and height that wraps content.
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        // Getting the recycler View
        return new NonUserListAdapter.UserListViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder holder, final int position) {

        // Setting the text view's name and phone to the the corresponding user present in the nonUserList.
        holder.mName.setText(nonUserList.get(position).getName());
        holder.mPhone.setText(nonUserList.get(position).getPhone());
        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String number = nonUserList.get(position).getPhone();  // The number on which you want to send SMS
                    Intent messageIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null));
                    messageIntent.putExtra("sms_body", "Hello, I want to invite you to start using WhatsUp....");
                    v.getContext().startActivity(messageIntent);
                } catch (Exception e) {
                    Toast.makeText(v.getContext(), "Sms not send", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.nonUserList.size();
    }
}
