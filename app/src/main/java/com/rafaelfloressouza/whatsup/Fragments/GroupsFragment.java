package com.rafaelfloressouza.whatsup.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rafaelfloressouza.whatsup.R;

import java.util.Map;


public class GroupsFragment extends Fragment {

    View viewToInflate;


    public GroupsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewToInflate = inflater.inflate(R.layout.groups_fragment, container, false);
        return viewToInflate;
    }

}
