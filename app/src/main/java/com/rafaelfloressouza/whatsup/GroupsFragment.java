package com.rafaelfloressouza.whatsup;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;


public class GroupsFragment extends Fragment {

    View viewToInflate;

    Map<String, String> groupMap;

    public GroupsFragment(Map<String, String> groupMap) {
        this.groupMap = groupMap;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewToInflate = inflater.inflate(R.layout.groups_fragment, container, false);
        return viewToInflate;
    }

}
