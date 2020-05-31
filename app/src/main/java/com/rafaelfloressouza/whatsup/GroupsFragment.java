package com.rafaelfloressouza.whatsup;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsFragment extends Fragment {

    View viewToInflate;

    public GroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewToInflate = inflater.inflate(R.layout.groups_fragment, container, false);
        return viewToInflate;
    }

}
